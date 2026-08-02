package github.axolotl.ai.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolExecutionRequestDO {
        String id;
        String name;
        String arguments;
        String sessionId;
        boolean isExecuted;
        long createdAt;
}
