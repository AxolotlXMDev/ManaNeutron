package github.axolotl.setting;

import java.util.List;

public class Settings {
        private List<Provider> providers;
        private String defaultProviderName;
        private String defaultModelId;
        private ReasoningEffort effort;
        private int maxRetryLimit;
        private int retryInterval;
}
