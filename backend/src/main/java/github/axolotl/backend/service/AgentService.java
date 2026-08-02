package github.axolotl.backend.service;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.invocation.InvocationContext;
import dev.langchain4j.model.chat.response.*;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutionResult;
import github.axolotl.ai.TokenUsageDO;
import github.axolotl.ai.content.AssistantContent;
import github.axolotl.ai.content.ToolContent;
import github.axolotl.ai.content.UserContent;
import github.axolotl.ai.session.Session;
import github.axolotl.ai.session.TaskStatus;
import github.axolotl.ai.sse.*;
import github.axolotl.backend.controller.SSEController;
import github.axolotl.backend.tool.DOUtil;
import github.axolotl.backend.tool.ReadFileTool;
import github.axolotl.backend.tool.TerminalTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static github.axolotl.ai.tool.Utils.getOrDefault;

/**
 * 负责提示词构建、AgentLoop、工具调用
 */
@Service
public class AgentService {
        @Autowired
        SessionManager sessionManager;

        @Autowired
        AIAPIService apiService;

        @Autowired
        SSEController sse;
        @Autowired
        private SessionService sessionService;
        @Autowired
        private DOUtil dOUtil;
        private StreamingChatResponseHandler handler;
        @Autowired
        private ToolExecutionRequestDOService toolExecutionRequestDOService;


        public void insertUserMessage(Session session, String content) throws IOException {
                session.addContent(new UserContent(content));
                sessionManager.updateAndSaveSession(session);
        }

        /**
         * 开启AgentLoop
         */
        public void startLoop(Session session, boolean isStartNewTask) {//TODO 增加监听器以支持回调更新客户端数据
                String sessionId = session.dgetId();
                TaskStatus task;
                if (isStartNewTask) {
                        task = new TaskStatus("new Task", System.currentTimeMillis(), false);
                } else {
                        task = session.dgetLastTaskStatus();
                        task.setFinished(false);
                }
                session.dsetLastTaskStatus(task);
                reloadHandler(session, sessionId);
                apiService.doChat(session, handler);
        }

        private void reloadHandler(Session session, String sessionId) {
                handler = new StreamingChatResponseHandler() {

                        @Override
                        public void onCompleteToolCall(CompleteToolCall completeToolCall) {
                                StreamingChatResponseHandler.super.onCompleteToolCall(completeToolCall);
                        }

                        @Override
                        public void onPartialResponse(String partialResponse) {
                                StreamingChatResponseHandler.super.onPartialResponse(partialResponse);
                                sse.sendEvent(sessionId, SSEName.PartialResponse, new PartialResponseDO(partialResponse));
                        }

                        @Override
                        public void onPartialThinking(PartialThinking partialThinking) {
                                StreamingChatResponseHandler.super.onPartialThinking(partialThinking);
                                sse.sendEvent(sessionId, SSEName.PartialThinking, new PartialThinkingDO(partialThinking.text()));
                        }

                        @Override
                        public void onPartialToolCall(PartialToolCall partialToolCall) {
                                sse.sendEvent(sessionId, SSEName.PartialToolCall, new PartialToolCallDO(
                                        partialToolCall.index(),
                                        partialToolCall.id(),
                                        partialToolCall.name(),
                                        partialToolCall.partialArguments()
                                ));
                                StreamingChatResponseHandler.super.onPartialToolCall(partialToolCall);
                        }

                        @Override
                        public void onCompleteResponse(ChatResponse completeResponse) {
                                AiMessage aiMessage = completeResponse.aiMessage();
                                TokenUsage tokenUsage = completeResponse.tokenUsage();
                                TokenUsageDO assistantResponseTokenUsageDO = new TokenUsageDO(tokenUsage.inputTokenCount(), tokenUsage.outputTokenCount());
                                List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();

                                session.addContent(new AssistantContent(
                                        getOrDefault(aiMessage.text(), ""),
                                        assistantResponseTokenUsageDO,
                                        toolExecutionRequests.stream().map(ToolExecutionRequest::id).toList()));


                                FinishReason finishReason = completeResponse.finishReason();

                                toolExecutionRequests.forEach(request -> {//先存isExecuted=false的在内存，便于之后拿取
                                        toolExecutionRequestDOService.addRequest(dOUtil.convertToDO(request, sessionId, false, System.currentTimeMillis()));
                                });
                                //SSE: AI回复完成
                                sse.sendEvent(sessionId, SSEName.CompleteResponse,
                                        new CompleteResponseDO(aiMessage.thinking(),
                                                aiMessage.text(),
                                                dOUtil.convertToDO(aiMessage.toolExecutionRequests(), sessionId, false, System.currentTimeMillis()),
                                                dOUtil.convertToDO(finishReason)
                                        )
                                );
                                //执行工具调用
                                toolExecutionRequests.forEach(request -> {
                                        Object tool = switch (request.name()) {
                                                case "read_file" -> new ReadFileTool();
                                                case "run_command" -> new TerminalTool();
                                                default ->
                                                        throw new IllegalArgumentException(
                                                                "未知工具: " + request.name()
                                                        );
                                        };
                                        //TODO 权限校验、审批
                                        DefaultToolExecutor executor = new DefaultToolExecutor(tool, request);

                                        ToolExecutionResult result = executor.executeWithContext(request,
                                                InvocationContext.builder()
                                                        .chatMemoryId(sessionId)
                                                        .build());
                                        //SSE: 工具调用结果
                                        sse.sendEvent(sessionId, SSEName.ToolCallResult, new ToolCallResultDO(
                                                result.isError(), result.result(), request.id()
                                        ));
                                        toolExecutionRequestDOService.addRequest(dOUtil.convertToDO(request, sessionId, true, System.currentTimeMillis()));
                                        session.addContent(new ToolContent(
                                                result.resultText(),
                                                request.id(),
                                                request.name(),
                                                !result.isError()
                                        ));

                                });
                                switch (finishReason) {
                                        case STOP -> {
                                                TokenUsageDO fullTokenUsageDO = new TokenUsageDO(completeResponse.tokenUsage().inputTokenCount(), completeResponse.tokenUsage().outputTokenCount());
                                                session.setTokenUsageDO(fullTokenUsageDO);

                                                TaskStatus task = session.dgetLastTaskStatus();
                                                task.setFinishedTime(System.currentTimeMillis());
                                                task.setFinished(true);
                                        }
                                        case TOOL_EXECUTION -> {
                                                reloadHandler(session, sessionId);
                                                apiService.doChat(session, handler);
                                        }
                                }
                                try {
                                        sessionManager.updateAndSaveSession(session);
                                        toolExecutionRequestDOService.saveRequests();
                                } catch (IOException e) {
                                        throw new RuntimeException(e);
                                }
                        }

                        @Override
                        public void onError(Throwable error) {
                                throw new RuntimeException(error);
                        }
                };
        }

}
