package github.axolotl.ai.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolExecutionRequestDOs {
        @Getter
        Map<String, ToolExecutionRequestDO> requests;

        public void addRequest(ToolExecutionRequestDO request) {
                requests.put(request.getId(), request);
        }
}
