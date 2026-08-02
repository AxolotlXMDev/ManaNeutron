package github.axolotl.ai.content;

import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@JSONType(typeName = "tool")
@NoArgsConstructor
public class ToolContent extends Content {
        private String requestId;
        private String name;
        private boolean isSuccess;

        public ToolContent(String content) {
                super(content);
        }

        public ToolContent(String content, String requestId, String name, boolean isSuccess) {
                super(content);
                this.requestId = requestId;
                this.name = name;
                this.isSuccess = isSuccess;
        }
}
