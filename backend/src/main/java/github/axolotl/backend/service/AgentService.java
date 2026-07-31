package github.axolotl.backend.service;

import github.axolotl.ai.content.UserContent;
import github.axolotl.ai.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 负责提示词构建、AgentLoop、工具调用
 */
@Service
public class AgentService {
        @Autowired
        SessionManager sessionManager;

        @Autowired
        PromptTemplateService promptTemplateService;

        @Autowired
        AIAPIService apiService;


        public void sendUserMessage(Session session, String content) throws IOException {
                session.getContents().add(new UserContent(content));
                sessionManager.updateAndSaveSession(session);
        }

        /**
         * 开启AgentLoop
         */
        public void startLoop(Session session){//TODO 增加监听器以支持回调更新客户端数据

        }

}
