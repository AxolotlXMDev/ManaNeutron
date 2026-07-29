package github.axolotl.ai.content;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ToolContent extends Content {
        private String id;
        private boolean isSuccess;

        public ToolContent(String content) {
                super(content);
        }

        public ToolContent(String content, String id, boolean isSuccess) {
                super(content);
                this.id = id;
                this.isSuccess = isSuccess;
        }
}
