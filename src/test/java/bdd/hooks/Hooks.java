package bdd.hooks;

import java.nio.file.Path;

import org.openqa.selenium.WebDriver;

import core.Config;
import core.DriverFactory;
import core.DriverManager;
import core.Screenshots;
import core.Waits;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void beforeScenario() {
        WebDriver driver = DriverFactory.createDriver();
        DriverManager.setDriver(driver);

        driver.get(Config.baseUrl());
        Waits.jsReady(driver);
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                WebDriver driver = DriverManager.getDriver();

                // Attach to Cucumber report
                byte[] png = Screenshots.asPngBytes(driver);
                scenario.attach(png, "image/png", "failure-screenshot");

                // Save file locally for debugging
                Path saved = Screenshots.savePng(driver, scenario.getName());
                scenario.log("Saved screenshot: " + saved.toString());
            }
        } finally {
            DriverManager.quitDriver();
        }
    }
}
