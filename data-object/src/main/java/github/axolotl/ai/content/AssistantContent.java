package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "assistant")
public class AssistantContent extends Content{
        public AssistantContent(String content) {
                super(content);
        }
}
