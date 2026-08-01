package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import github.axolotl.ai.TokenUsageDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "assistant")
@NoArgsConstructor
public class AssistantContent extends Content {
        TokenUsageDO tokenUsageDO;

        public AssistantContent(String content) {
                super(content);
        }

        public AssistantContent(String content, TokenUsageDO tokenUsageDO) {
                super(content);
                this.tokenUsageDO = tokenUsageDO;
        }
}
