package github.axolotl.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelInfo {
        private ModelId modelId;

        private long contextLimit;
        private boolean supportImageInput;
}
