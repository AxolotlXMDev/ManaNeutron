package github.axolotl.setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelId {
        private String name;
        private String ModelId;

        public ModelId(String modelId) {
                ModelId = modelId;
                name = modelId;
        }
}
