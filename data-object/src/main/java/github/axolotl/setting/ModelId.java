package github.axolotl.setting;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelId {
        private String name;
        private String ModelId;

        public ModelId(String modelId) {
                ModelId = modelId;
                name = modelId;
        }
}
