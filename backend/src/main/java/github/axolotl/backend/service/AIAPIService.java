package github.axolotl.backend.service;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.json.*;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.internal.chat.AssistantMessage;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import github.axolotl.ai.content.AssistantContent;
import github.axolotl.ai.content.SystemContent;
import github.axolotl.ai.content.ToolContent;
import github.axolotl.ai.content.UserContent;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.tool.ToolDefinition;
import github.axolotl.ai.tool.ToolParameter;
import github.axolotl.backend.tool.ReadFileTool;
import github.axolotl.setting.ModelChoice;
import github.axolotl.setting.Provider;
import github.axolotl.setting.Settings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        private OpenAiStreamingChatModel buildChatModel(Session session) {
                ModelChoice modelChoice = session.getInfo().getModelChoice();
                Provider provider = providerService.getProviderByName(modelChoice.getProviderName());
                Settings settings = settingsService.getSettings();
                OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                        .baseUrl(provider.getBaseUrl())
                        .returnThinking(true)
                        .apiKey(provider.getApiKey())
                        .modelName(modelChoice.getModelId().getModelId())
                        .reasoningEffort(modelChoice.getEffort().toString().toLowerCase())
//                        .maxRetries(settings.getMaxRetryLimit())
                        .build();
                return model;
        }

        public void doChat(Session session, StreamingChatResponseHandler handler) {
                OpenAiStreamingChatModel model = buildChatModel(session);
                ChatRequest request = buildChatRequest(session);
                model.doChat(request, handler);
        }

        private List<ToolSpecification> getToolSpecifications() {
                List<ToolSpecification> tools = new ArrayList<>();
                List<ToolSpecification> readFileTool = ToolSpecifications.toolSpecificationsFrom(ReadFileTool.class);

                tools.addAll(readFileTool);
                return tools;
        }

        private ChatRequest buildChatRequest(Session session) {
                ModelChoice modelChoice = session.getInfo().getModelChoice();
                OpenAiChatRequestParameters parameters = OpenAiChatRequestParameters.builder()
                        .modelName(modelChoice.getModelId().getModelId())
                        .reasoningEffort(modelChoice.getEffort().toString().toLowerCase())
                        .build();

                ChatRequest request = ChatRequest.builder()
                        .messages(buildChatMessages(session))
                        .toolSpecifications(getToolSpecifications())
                        .parameters(parameters)
                        .build();
                return request;
        }

        private List<ChatMessage> buildChatMessages(Session session) {
                List<ChatMessage> messages = new ArrayList<>();
                session.getContents().forEach(c -> {
                                String content = c.getContent();
                                messages.add(switch (c) {
                                        case UserContent ignored -> UserMessage.from(content);
                                        case SystemContent ignored -> SystemMessage.from(content);
                                        case AssistantContent ignored -> AiMessage.from(content);
                                        case ToolContent tool ->
                                                ToolExecutionResultMessage.from(tool.getId(), tool.getName(), tool.getContent());

                                        default ->
                                                throw new IllegalStateException("Unexpected value: " + c);
                                });

                        }
                );
                return messages;
        }

        private ToolDefinition parseToolDefinition(ToolSpecification spec) {
                ToolDefinition definition = new ToolDefinition();
                definition.setName(spec.name());
                definition.setDescription(spec.description());

                JsonObjectSchema paramsSchema = (JsonObjectSchema) spec.parameters();
                List<String> requiredNames = paramsSchema.required(); // 必填参数名列表

                Map<String, JsonSchemaElement> properties = paramsSchema.properties();
                List<ToolParameter> parameters = new ArrayList<>();

                if (properties != null) {
                        for (Map.Entry<String, JsonSchemaElement> entry : properties.entrySet()) {
                                String paramName = entry.getKey();
                                JsonSchemaElement element = entry.getValue();
                                ToolParameter parameter = parseToolParameter(paramName, element);
                                // 标记是否必填
                                parameter.setRequired(requiredNames != null && requiredNames.contains(paramName));
                                parameters.add(parameter);
                        }
                }
                definition.setParameters(parameters);
                return definition;
        }

        private ToolParameter parseToolParameter(String name, JsonSchemaElement
                element) {
                ToolParameter parameter = new ToolParameter();
                parameter.setName(name);
                parameter.setDescription(element.description());

                switch (element) {
                        case JsonStringSchema ignored ->
                                parameter.setType(ToolParameter.Type.STRING);
                        case JsonIntegerSchema ignored ->
                                parameter.setType(ToolParameter.Type.INTEGER);
                        case JsonNumberSchema ignored ->
                                parameter.setType(ToolParameter.Type.NUMBER);
                        case JsonBooleanSchema ignored ->
                                parameter.setType(ToolParameter.Type.BOOLEAN);
                        case JsonEnumSchema enumSchema -> {
                                parameter.setType(ToolParameter.Type.ENUM);
                                parameter.setEnumValues(enumSchema.enumValues());
                        }
                        case JsonObjectSchema objectSchema -> {
                                parameter.setType(ToolParameter.Type.OBJECT);
                                // 递归解析对象内部的属性
                                Map<String, JsonSchemaElement> props = objectSchema.properties();
                                List<String> objRequired = objectSchema.required();
                                if (props != null) {
                                        List<ToolParameter> nestedParams = new ArrayList<>();
                                        for (Map.Entry<String, JsonSchemaElement> entry : props.entrySet()) {
                                                ToolParameter child = parseToolParameter(entry.getKey(), entry.getValue());
                                                child.setRequired(objRequired != null && objRequired.contains(entry.getKey()));
                                                nestedParams.add(child);
                                        }
                                        parameter.setProperties(nestedParams);
                                }
                        }
                        case JsonArraySchema arraySchema -> {
                                parameter.setType(ToolParameter.Type.ARRAY);
                                // 递归解析数组元素类型
                                ToolParameter itemType = parseToolParameter(null, arraySchema.items());
                                parameter.setArrayType(itemType);
                        }
                        default ->
                                throw new IllegalArgumentException("Unsupported schema element: " + element.getClass());
                }
                return parameter;
        }
//        private ToolParameter buildToolParameter(JsonSchemaElement schema) {
//
//        }
}
