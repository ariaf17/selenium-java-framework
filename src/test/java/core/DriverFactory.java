package core;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;


public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriver() {
        String browser = Config.browser();
        boolean headless = Config.headless();

        WebDriver driver;

        switch (browser) {
            case "chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                options.addArguments("--headless=new");
                }
                options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");

                driver = new ChromeDriver(options);

                String profileDir = Paths.get("target", "chrome-profile", UUID.randomUUID().toString()).toString();
                options.addArguments("--user-data-dir=" + profileDir);

                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);

                // Reduce other “services” prompts
                prefs.put("profile.default_content_setting_values.notifications", 2);

                options.setExperimentalOption("prefs", prefs);

                
            }
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                driver = new FirefoxDriver(options);
                driver.manage().window().maximize();
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    // Edge uses Chromium; headless should work similarly
                    options.addArguments("--headless=new");
                }
                driver = new EdgeDriver(options);
                driver.manage().window().maximize();
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser + " (use chrome|firefox|edge)");
        }

        // Keep implicit waits OFF; we will use explicit waits only.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Math.max(30, Config.timeoutSeconds() * 3)));

        return driver;
    }
}
