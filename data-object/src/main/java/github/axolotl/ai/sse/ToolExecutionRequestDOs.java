package github.axolotl.ai.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolExecutionRequestDOs {
        @Getter
        List<ToolExecutionRequestDO> requests;

        public void addRequest(ToolExecutionRequestDO request) {
                requests.add(request);
        }
}
