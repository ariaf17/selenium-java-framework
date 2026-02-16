package core;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public final class Waits {

    private Waits() {}

    private static FluentWait<WebDriver> waitOf(WebDriver driver) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Config.timeoutSeconds()))
                .pollingEvery(Duration.ofMillis(Config.pollMillis()))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public static WebElement visible(WebDriver driver, By locator) {
        return waitOf(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(WebDriver driver, By locator) {
        return waitOf(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean urlContains(WebDriver driver, String fragment) {
        return waitOf(driver).until(ExpectedConditions.urlContains(fragment));
    }

    public static boolean titleContains(WebDriver driver, String fragment) {
        return waitOf(driver).until(ExpectedConditions.titleContains(fragment));
    }

    public static void jsReady(WebDriver driver) {
        waitOf(driver).until(d -> {
            try {
                Object state = ((JavascriptExecutor) d).executeScript("return document.readyState");
                return "complete".equals(state);
            } catch (Exception e) {
                return true; // don’t hard-fail if JS is blocked
            }
        });
    }
}
