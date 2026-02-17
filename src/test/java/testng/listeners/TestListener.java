package testng.listeners;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import core.DriverManager;
import core.Screenshots;
import io.qameta.allure.Allure;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            WebDriver driver = DriverManager.getDriver();

            // Attach to Allure
            byte[] png = Screenshots.asPngBytes(driver);
            Allure.addAttachment("Failure Screenshot", "image/png", new ByteArrayInputStream(png), ".png");

            // Save locally too
            Path saved = Screenshots.savePng(driver, result.getName());
            Allure.addAttachment("Saved Screenshot Path", saved.toString());
        } catch (Exception ignored) {
            // avoid masking original failure
        }
    }
}