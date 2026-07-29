package github.axolotl.config;

import java.util.List;

public class Settings {
        private List<Provider> providers;
        private String defaultProviderName;
        private ReasoningEffort effort;
        private int maxRetryLimit;
        private int retryInterval;
}
