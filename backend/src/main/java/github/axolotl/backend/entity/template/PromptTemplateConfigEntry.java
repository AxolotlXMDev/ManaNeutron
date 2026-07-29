package github.axolotl.backend.entity.template;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PromptTemplateConfigEntry {
        private int order;
        private PromptTemplateType type;
        private String filePath;


}
