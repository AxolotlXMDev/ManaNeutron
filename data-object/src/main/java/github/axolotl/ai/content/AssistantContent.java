package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import github.axolotl.ai.TokenUsageDO;
import github.axolotl.ai.sse.ToolExecutionRequestDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "assistant")
@NoArgsConstructor
public class AssistantContent extends Content {
        TokenUsageDO tokenUsageDO;
        List<String> requestIds;

        public AssistantContent(String content, String thinking, List<String> requestIds) {
                super(content);
                this.requestIds = requestIds;
        }

        public AssistantContent(String content, TokenUsageDO tokenUsageDO, List<String> requestIds) {
                super(content);
                this.tokenUsageDO = tokenUsageDO;
                this.requestIds = requestIds;
        }


}
