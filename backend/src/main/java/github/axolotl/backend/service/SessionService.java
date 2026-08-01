package github.axolotl.backend.service;

import github.axolotl.ai.content.Content;
import github.axolotl.ai.content.UserContent;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
import github.axolotl.backend.entity.template.PromptTemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于组合其他Service，向上提供给Controller
 * 简单方法请直接调用SessionManager
 */
@Service
public class SessionService {
        @Autowired
        SessionManager sessionManager;

        @Autowired
        PromptTemplateService promptTemplateService;

        public SessionInfos getSessionInfos() {
                return sessionManager.getSessionInfos();
        }

        public Session createSession(SessionInfo sessionInfo) throws IOException {
                List<Content> initSystemContents = promptTemplateService.loadPromptTemplateToContentByType(PromptTemplateType.INIT);
                ArrayList<Content> contents = new ArrayList<>(initSystemContents);
                Session newSession = Session.builder()
                        .info(sessionInfo)
                        .contents(contents)
                        .build();
                sessionManager.updateAndSaveSession(newSession);
                return newSession;
        }

        public SessionInfo getLastSessionInfo() {
                List<SessionInfo> sessionInfoList = getSessionInfos().getSessionInfos();
                if (sessionInfoList.isEmpty()) {
                        return null;
                }
                sessionInfoList.sort((a, b) -> Long.compare(b.getUpdateTime(), a.getUpdateTime()));
                return sessionInfoList.getFirst();
        }

}
