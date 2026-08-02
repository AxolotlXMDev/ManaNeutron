package github.axolotl.ai.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteResponseDO {
        String thinking;
        String response;
        List<ToolExecutionRequestDO> toolCalls;
        FinishReasonDO  finishReason;
}
