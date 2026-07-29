package github.axolotl.backend.entity.template;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
public class PromptTemplateConfigEntries {
        private List<PromptTemplateConfigEntry> entries;

        public boolean isValid() {
                if (entries == null) return true;
                for (PromptTemplateConfigEntry entry : entries) {
                        if (!new File(entry.getFilePath()).exists()) {
                                return false;
                        }
                }
                return true;
        }

}
