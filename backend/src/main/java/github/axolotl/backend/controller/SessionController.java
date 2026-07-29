package github.axolotl.backend.controller;

import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.ai.session.SessionInfos;
import github.axolotl.backend.service.SessionService;
import github.axolotl.backend.service.SettingsService;
import github.axolotl.setting.ModelChoice;
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

        @PostMapping("/newSession")
        public Session newSession(@RequestBody String workDir, @RequestBody ModelChoice modelChoice) throws IOException {
                SessionInfo sessionInfo = SessionInfo.builder()
                        .workDir(workDir)
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .modelChoice(modelChoice)
                        .name("New Session")
                        .id(UUID.randomUUID().toString())
                        .build();
                return sessionService.createSession(sessionInfo);
        }

        @GetMapping("/getLastSessionInfo")
        public SessionInfo getLastSessionInfo() {
                return sessionService.getLastSessionInfo();
        }

}
