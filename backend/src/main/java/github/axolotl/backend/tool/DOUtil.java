package github.axolotl.backend.tool;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.model.output.FinishReason;
import github.axolotl.ai.sse.FinishReasonDO;
import github.axolotl.ai.sse.ToolExecutionRequestDO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DOUtil {
        public ToolExecutionRequestDO convertToDO(ToolExecutionRequest toolExecutionRequest, String sessionId,boolean isExecuted, long createdAt) {
                return new ToolExecutionRequestDO(
                        toolExecutionRequest.id(),
                        toolExecutionRequest.name(),
                        toolExecutionRequest.arguments(),
                        sessionId,
                        isExecuted,
                        createdAt
                );
        }

        public List<ToolExecutionRequestDO> convertToDO(List<ToolExecutionRequest> toolExecutionRequests, String sessionId, boolean isExecuted, long createdAt) {
                return toolExecutionRequests.stream().map(request -> this.convertToDO(request, sessionId, isExecuted, createdAt)).toList();
        }

        public FinishReasonDO convertToDO(FinishReason finishReason) {
                return FinishReasonDO.valueOf(finishReason.name());
        }

        public ToolExecutionRequest convertToToolExecutionRequest(ToolExecutionRequestDO toolExecutionRequestDO) {
                return ToolExecutionRequest.builder()
                        .id(toolExecutionRequestDO.getId())
                        .name(toolExecutionRequestDO.getName())
                        .arguments(toolExecutionRequestDO.getArguments())
                        .build();
        }

        public List<ToolExecutionRequest> convertToToolExecutionRequest(List<ToolExecutionRequestDO> toolExecutionRequestDOs) {
                return toolExecutionRequestDOs.stream().map(this::convertToToolExecutionRequest).toList();
        }
}