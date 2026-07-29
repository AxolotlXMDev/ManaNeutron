package github.axolotl.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Settings {
        private List<Provider> providers;
        private String defaultProviderName;
        private ModelId modelId;
        private ReasoningEffort effort;
        private int maxRetryLimit;
        private int retryInterval;

        public static Settings getDefault() {
                return Settings.builder()
                        .providers(List.of(Provider.getDefault()))
                        .defaultProviderName("defaultProviderName")
                        .modelId(new ModelId("defaultModelId"))
                        .effort(ReasoningEffort.MEDIUM)
                        .maxRetryLimit(5)
                        .retryInterval(5)
                        .build();
        }

        public boolean valid() {
                if (providers == null || providers.isEmpty()) {
                        return false;
                }
                if (defaultProviderName == null || defaultProviderName.isEmpty()) {
                        return false;
                }
                if (modelId == null || modelId.getModelId().isEmpty()) {
                        return false;
                }
                if (effort == null) {
                        return false;
                }
                if (maxRetryLimit < 0) {
                        return false;
                }
                if (retryInterval < 0) {
                        return false;
                }
                if (providers.stream()
                        .map(Provider::getName)
                        .filter(defaultProviderName::equals)
                        .findFirst().isEmpty()) {
                        return false;
                }
                if (providers.stream()
                        .filter(provider -> defaultProviderName.equals(provider.getName()))
                        .findFirst().get()

                        //不通过find去找、因为反序列化出来的是2给不同的ModelId
                        .getModelIds().stream().map(ModelId::getModelId)
                        .filter(modelId->modelId.equals(this.modelId.getModelId())).findFirst().isEmpty()
                ) {
                        return false;
                }

                return true;
        }
}
