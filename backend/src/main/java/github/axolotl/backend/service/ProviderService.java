package github.axolotl.backend.service;

import github.axolotl.setting.Provider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {
        private final SettingsService settingsService;

        public ProviderService(SettingsService settingsService) {
                this.settingsService = settingsService;
        }

        public Provider getProviderByName(String name) {
                List<Provider> providers = settingsService.getSettings().getProviders();
                for (Provider provider : providers) {
                        if (provider.getName().equalsIgnoreCase(name)) {
                                return provider;
                        }
                }
                return null;
        }
}
