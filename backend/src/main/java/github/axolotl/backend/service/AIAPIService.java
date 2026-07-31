package github.axolotl.backend.service;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.openai.OpenAiChatModel;
import github.axolotl.ai.session.Session;
import github.axolotl.setting.ModelChoice;
import github.axolotl.setting.Provider;
import github.axolotl.setting.Settings;
import org.springframework.stereotype.Service;

/**
 * 负责和AI API的交互
 */
@Service
public class AIAPIService {
        private final SettingsService settingsService;
        private final ProviderService providerService;

        public AIAPIService(SettingsService settingsService, ProviderService providerService) {
                this.settingsService = settingsService;
                this.providerService = providerService;
        }

        private ChatModel buildChatModel(Session session) {
                ModelChoice modelChoice = session.getInfo().getModelChoice();
                Provider provider = providerService.getProviderByName(modelChoice.getProviderName());
                Settings settings = settingsService.getSettings();
                OpenAiChatModel model = OpenAiChatModel.builder()
                        .baseUrl(provider.getBaseUrl())
                        .returnThinking(true)
                        .apiKey(provider.getApiKey())
                        .modelName(modelChoice.getModelId().getModelId())
                        .reasoningEffort(modelChoice.getEffort().toString().toLowerCase())
                        .maxRetries(settings.getMaxRetryLimit())
                        .build();
                return model;
        }

        private ChatRequest buildChatRequest(Session session) {
                ChatRequest request = ChatRequest.builder()
                        .messages()//TODO Content到框架的转换
                        .toolSpecifications()//TODO 工具的添加
                        .build();
                return request;
        }
}
