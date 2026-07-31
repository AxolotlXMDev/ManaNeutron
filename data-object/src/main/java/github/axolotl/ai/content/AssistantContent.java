package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "assistant")
@NoArgsConstructor
public class AssistantContent extends Content{
        public AssistantContent(String content) {
                super(content);
        }
}
