package github.axolotl.backend.controller;

import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
import github.axolotl.backend.service.SessionManager;
import github.axolotl.backend.service.SessionService;
import github.axolotl.setting.ModelChoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
public class SessionController {
        @Autowired
        SessionService sessionService;
        @Autowired
        SessionManager sessionManager;

        @GetMapping("/getSessionInfos")
        public SessionInfos getSessionInfos() {
                return sessionService.getSessionInfos();
        }

        public record CreateSessionRequest(
                String name,
                String workDir,
                ModelChoice modelChoice
        ) {
        }

        @PostMapping("/newSession")
        public Session newSession(@RequestBody CreateSessionRequest request) throws IOException {
                SessionInfo sessionInfo = SessionInfo.builder()
                        .workDir(request.workDir)
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .modelChoice(request.modelChoice)
                        .name(request.name)
                        .id(UUID.randomUUID().toString())
                        .build();
                return sessionService.createSession(sessionInfo);
        }

        @GetMapping("/getLastSessionInfo")
        public SessionInfo getLastSessionInfo() {
                return sessionService.getLastSessionInfo();
        }


        @GetMapping("/getSessionById")
        public Session getSessionById(@RequestParam String sessionId) {
                return sessionManager.getSessionById(sessionId);
        }

        public record EditSessionRequest(
                String sessionId,
                String contentIndex,
                String content
        ) {
        }

        @PostMapping("/editContent")
        public boolean editContent(@RequestBody EditSessionRequest request) throws IOException {
                Session session = sessionManager.getSessionById(request.sessionId);
                session.getContents().get(Integer.parseInt(request.contentIndex)).setContent(request.content);
                sessionManager.updateAndSaveSession(session);
                return true;
        }

        @GetMapping("/sendUserMessage")
        public boolean sendUserMessage(@RequestParam String sessionId, @RequestParam String content) throws IOException {
                Session session = sessionManager.getSessionById(sessionId);
                sessionService.sendUserMessage(session, content);
                sessionManager.updateAndSaveSession(session);
                return true;
        }

}
