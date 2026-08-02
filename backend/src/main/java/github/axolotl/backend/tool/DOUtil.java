package github.axolotl.backend.tool;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.model.output.FinishReason;
import github.axolotl.ai.sse.FinishReasonDO;
import github.axolotl.ai.sse.ToolExecutionRequestDO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DOUtil {
        public ToolExecutionRequestDO convertToDO(ToolExecutionRequest toolExecutionRequest) {
                return new ToolExecutionRequestDO(
                        toolExecutionRequest.id(),
                        toolExecutionRequest.name(),
                        toolExecutionRequest.arguments()
                );
        }

        public List<ToolExecutionRequestDO> convertToDO(List<ToolExecutionRequest> toolExecutionRequests) {
                return toolExecutionRequests.stream().map(this::convertToDO).toList();
        }
        public FinishReasonDO convertToDO(FinishReason finishReason) {
                return  FinishReasonDO.valueOf(finishReason.name());
        }
}