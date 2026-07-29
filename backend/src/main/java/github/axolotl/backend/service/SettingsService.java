package github.axolotl.backend.service;

import com.alibaba.fastjson2.JSONObject;
import dczx.axolotl.util.file.FilesUtil;
import github.axolotl.setting.Settings;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SettingsService {
        @Value("${mana-neutron.config.setting-path}")
        private String settingPath;
        @Getter
        private Settings settings;

        @PostConstruct
        public void init() throws IOException {
                loadSettings();
        }

        private void loadSettings() throws IOException {
                FilesUtil.keepFileExists(settingPath);
                String text = Files.readString(Path.of(settingPath));
                settings = JSONObject.parseObject(text, Settings.class);
                if (settings == null || settings.getProviders() == null || settings.getProviders().isEmpty()) {
                        settings = Settings.getDefault();
                }
                if (!settings.valid()) {
                        throw new IllegalArgumentException("Invalid settings");
                }
                saveSettings(settings);
        }

        private void updateSettings(Settings settings) {
                this.settings = settings;
        }

        public void saveSettings(Settings settings) {
                updateSettings(settings);
                saveSettings();
        }

        public void saveSettings() {
                try {
                        Files.writeString(Path.of(settingPath), JSONObject.toJSONString(settings));
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }

}
