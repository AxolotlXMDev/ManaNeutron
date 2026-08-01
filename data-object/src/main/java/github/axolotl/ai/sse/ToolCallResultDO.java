package github.axolotl.ai.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolCallResultDO {
        boolean isError;
        Object result;
        ToolExecutionRequestDO request;
}
