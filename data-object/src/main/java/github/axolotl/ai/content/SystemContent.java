package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "system")
public class SystemContent extends Content{
        public SystemContent(String content) {
                super(content);
        }
}
