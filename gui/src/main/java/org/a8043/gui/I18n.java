package org.a8043.gui;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class I18n {
        private static final Locale locale = Locale.getDefault();
        @Getter
        private static final ResourceBundle langBundle =
                ResourceBundle.getBundle("languages.messages", locale);

        static {
                log.info("Language: {}", locale.getDisplayName());
        }

        public static String get(String key, String... args) {
                String str;
                try {
                        str = langBundle.getString(key);
                } catch (Exception e) {
                        log.warn("Missing i18n key: {}", key);
                        str = key;
                }
                return MessageFormat.format(str, (Object[]) args);
        }
}
