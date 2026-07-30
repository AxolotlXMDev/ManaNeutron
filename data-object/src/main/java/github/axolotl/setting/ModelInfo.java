package github.axolotl.setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelInfo {
        private ModelId modelId;

        private long contextLimit;
        private boolean supportImageInput;
}
