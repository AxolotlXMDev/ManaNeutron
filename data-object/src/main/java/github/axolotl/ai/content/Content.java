package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JSONType(
        typeKey = "type",
        seeAlso = {
                UserContent.class,
                SystemContent.class,
                ToolContent.class,
                AssistantContent.class
        }
)
public abstract class Content {
        private String content;
}
