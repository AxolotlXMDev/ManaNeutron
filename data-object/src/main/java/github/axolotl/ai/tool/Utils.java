package github.axolotl.ai.tool;

public class Utils {
        public static <T> T getOrDefault(T value, T defaultValue) {
                return value != null ? value : defaultValue;
        }
}
