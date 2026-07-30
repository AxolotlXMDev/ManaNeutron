package github.axolotl.setting;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储模型选择状态，如：OpenRouter/gpt5.5/high
 * 请求时：通过name去获取提供商具体信息以构建完整的request参数
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ModelChoice {
        private String providerName;
        private ModelId modelId;
        private ReasoningEffort effort;
}
