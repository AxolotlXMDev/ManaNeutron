package github.axolotl.backend.controller;

import github.axolotl.backend.service.SettingsService;
import github.axolotl.setting.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
public class SettingsController {

        @Autowired
        private SettingsService settingsService;

        @GetMapping("/getSetting")
        public Settings getSetting() {
                return settingsService.getSettings();
        }
}
