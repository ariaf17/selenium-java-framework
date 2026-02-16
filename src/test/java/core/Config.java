package core;

import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public final class Config {

    private static final Properties FILE_PROPS = new Properties();
    private static final String ENV;

    static {
        ENV = System.getProperty("env", "local").trim().toLowerCase(Locale.ROOT);
        String resourcePath = "config/" + ENV + ".properties";

        try (InputStream is = Config.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found on classpath: " + resourcePath);
            }
            FILE_PROPS.load(is);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load config: " + resourcePath, e);
        }
    }

    private Config() {}

    public static String env() {
        return ENV;
    }

    /**
     * Precedence:
     * 1) -Dkey=value JVM system properties
     * 2) ENV VAR (KEY, KEY_WITH_UNDERSCORES)
     * 3) properties file (src/test/resources/config/{env}.properties)
     */
    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) return sys.trim();

        String env1 = System.getenv(key);
        if (env1 != null && !env1.isBlank()) return env1.trim();

        String env2 = System.getenv(key.toUpperCase(Locale.ROOT).replace('.', '_'));
        if (env2 != null && !env2.isBlank()) return env2.trim();

        String file = FILE_PROPS.getProperty(key);
        if (file != null && !file.isBlank()) return file.trim();

        return null;
    }

    public static String getRequired(String key) {
        String val = get(key);
        if (val == null) {
            throw new IllegalStateException("Missing required config key: " + key + " (env=" + ENV + ")");
        }
        return val;
    }

    public static String baseUrl() {
        return getRequired("baseUrl");
    }

    public static String browser() {
        return Objects.requireNonNullElse(get("browser"), "chrome").toLowerCase(Locale.ROOT);
    }

    public static boolean headless() {
        return Boolean.parseBoolean(Objects.requireNonNullElse(get("headless"), "false"));
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(Objects.requireNonNullElse(get("timeoutSeconds"), "10"));
    }

    public static long pollMillis() {
        return Long.parseLong(Objects.requireNonNullElse(get("pollMillis"), "200"));
    }
}
