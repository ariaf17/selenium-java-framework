package core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void shouldLoadEnvironmentConfig() {
        assertNotNull(Config.env());
        assertFalse(Config.env().isBlank());

        String baseUrl = Config.baseUrl();
        assertNotNull(baseUrl);
        assertTrue(baseUrl.startsWith("http"));

        String browser = Config.browser();
        assertNotNull(browser);
        assertFalse(browser.isBlank());

        assertTrue(Config.timeoutSeconds() > 0);
    }
}
