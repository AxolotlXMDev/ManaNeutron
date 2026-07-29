package github.axolotl.backend.entity.template;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class PromptTemplate {
        private int order;
        private PromptTemplateType type;
        private String content;

        public String putVariables(Map<String, Object> variables) {
                for (Map.Entry<String, Object> entry : variables.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (value != null) {
                                content = content.replace("${{" + key + "}}", value.toString());
                        }
                }
                return content;
        }
}
