package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "user")
public class UserContent extends Content{
        public UserContent(String content) {
                super(content);
        }
}
