package github.axolotl.backend.service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import dczx.axolotl.util.file.FilesUtil;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 此类负责与本地文件的session交互、提供方法供SessionService使用
 */
@Service
public class SessionManager {
        @Value("${mana-neutron.config.session-path}")
        String sessionPath;
        @Value("${mana-neutron.config.session-cache-path}")
        String sessionCachePath;

        final HashMap<String, Session> loadedSession = new HashMap<>();
        @Getter
        SessionInfos sessionInfos;

        @PostConstruct
        public void init() throws IOException {
                FilesUtil.keepFolderExists(sessionPath);
                FilesUtil.keepFileExists(sessionCachePath);
                String text = Files.readString(Path.of(sessionCachePath));
                sessionInfos = JSONObject.parseObject(text, SessionInfos.class);
                if (sessionInfos == null || sessionInfos.getSessionInfos() == null) {
                        sessionInfos = new SessionInfos(new ArrayList<>());
                }
        }

        public Session loadSession(String sessionId) throws IOException {
                String text = Files.readString(getSessionFilePath(sessionId));
                return updateSession(JSONObject.parseObject(text, Session.class));
        }

        private @NonNull Path getSessionFilePath(String sessionId) {
                return Path.of(sessionPath).resolve(sessionId + ".json");
        }

        /**
         * 在"Session初始化、用户发送消息、AI回复"时保存一次
         */
        public void saveSession(Session session) throws IOException {
                Files.writeString(
                        getSessionFilePath(session.getId()),
                        JSONObject.toJSONString(session, JSONWriter.Feature.WriteClassName)
                );
        }

        public void updateAndSaveSession(Session session) throws IOException {
                updateSession(session);
                saveSession(session);
        }

        public Session updateSession(Session session) {
                String sessionId = session.getInfo().getId();
                if (!loadedSession.containsKey(sessionId)) {
                        sessionInfos.addSessionInfo(session);
                }
                loadedSession.put(sessionId, session);
                sessionInfos.updateSessionInfo(session.getInfo());
                return session;
        }

        public Session getSessionById(String sessionId) {
                return loadedSession.get(sessionId);
        }

        public void addSessionInfo(SessionInfo sessionInfo) {
                sessionInfos.addSessionInfo(sessionInfo);
        }


}
