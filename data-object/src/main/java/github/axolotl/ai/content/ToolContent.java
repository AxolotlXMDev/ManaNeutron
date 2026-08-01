package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "tool")
@NoArgsConstructor
public class ToolContent extends Content {
        private String id;
        private String name;
        private boolean isSuccess;

        public ToolContent(String content) {
                super(content);
        }

        public ToolContent(String content, String id, String name, boolean isSuccess) {
                super(content);
                this.id = id;
                this.name = name;
                this.isSuccess = isSuccess;
        }
}
