package github.axolotl.setting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Provider {
        private String name;
        private String apiKey;
        private String baseUrl;
        private boolean isEnabled;

        private List<ModelId> modelIds;

        public static Provider getDefault() {
                return new Provider(
                        "defaultProviderName", "api-key",
                        "https://openrouter.ai/api/v1", true,
                        List.of(new ModelId("defaultModelId"))
                );
        }
}
