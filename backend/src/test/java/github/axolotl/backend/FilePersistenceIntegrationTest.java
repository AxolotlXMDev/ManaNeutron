package github.axolotl.backend;

import github.axolotl.ai.content.SystemContent;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.SessionInfo;
import github.axolotl.backend.entity.template.PromptTemplateType;
import github.axolotl.backend.service.PromptTemplateService;
import github.axolotl.backend.service.SessionManager;
import github.axolotl.backend.service.SettingsService;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class FilePersistenceIntegrationTest {
        @Value("${mana-neutron.config.setting-path}")
        private String settingPath;

        @Value("${mana-neutron.config.session-path}")
        private String sessionPath;

        @Autowired
        private SettingsService settingsService;
        @Autowired
        private PromptTemplateService promptTemplateService;
        @Autowired
        private SessionManager sessionManager;

        @Test
        void writesInspectableDataOnlyToTheTestSandbox() throws Exception {
                Path normalizedSettingPath = Path.of(settingPath).normalize();
                assertTrue(Files.size(normalizedSettingPath) > 0);
                assertTrue(settingsService.getSettings().valid());
                assertFalse(promptTemplateService.getTemplatesByType(PromptTemplateType.INIT).isEmpty());

                String sessionId = "test-" + UUID.randomUUID();
                Session session = Session.builder()
                        .info(SessionInfo.builder()
                                .id(sessionId)
                                .name("JUnit debug session")
                                .workDir(".")
                                .createTime(System.currentTimeMillis())
                                .updateTime(System.currentTimeMillis())
                                .build())
                        .contents(List.of(new SystemContent("Hello")))
                        .todos(List.of())
                        .build();

                sessionManager.updateAndSaveSession(session);

                Path sessionFile = Path.of(sessionPath).resolve(sessionId + ".json");
                assertTrue(Files.exists(sessionFile));
                assertEquals(sessionId, sessionManager.getSession(sessionId).getId());
                System.out.println("Inspectable test data: " + sessionFile.toAbsolutePath());
        }

        @Test
        void readData() throws Exception {
                writesInspectableDataOnlyToTheTestSandbox();
                List<SessionInfo> sessionInfos = sessionManager.getSessionInfos().getSessionInfos();
                assertEquals(sessionInfos.size(), 1);
                String sessionId = sessionInfos.get(0).getId();
                Session session = sessionManager.loadSession(sessionId);
                assertEquals(session.getContents().size(), 1);
                assertInstanceOf(SystemContent.class, session.getContents().getFirst());
        }
}
