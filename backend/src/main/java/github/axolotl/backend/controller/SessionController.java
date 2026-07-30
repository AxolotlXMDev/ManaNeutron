package github.axolotl.backend.controller;

import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
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

        @GetMapping("/getSessionInfos")
        public SessionInfos getSessionInfos() {
                return sessionService.getSessionInfos();
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CreateSessionRequest {
                private String name;
                private String workDir;
                private ModelChoice modelChoice;
        }

        @PostMapping("/newSession")
        public Session newSession(@RequestBody CreateSessionRequest createSessionRequest) throws IOException {
                SessionInfo sessionInfo = SessionInfo.builder()
                        .workDir(createSessionRequest.workDir)
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .modelChoice(createSessionRequest.modelChoice)
                        .name(createSessionRequest.name)
                        .id(UUID.randomUUID().toString())
                        .build();
                return sessionService.createSession(sessionInfo);
        }

        @GetMapping("/getLastSessionInfo")
        public SessionInfo getLastSessionInfo() {
                return sessionService.getLastSessionInfo();
        }

}
