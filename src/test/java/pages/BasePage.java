package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import core.DriverManager;
import core.Waits;

public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    protected WebElement visible(By locator) {
        return Waits.visible(driver, locator);
    }

    protected WebElement clickable(By locator) {
        return Waits.clickable(driver, locator);
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText();
    }

    public String title() {
        return driver.getTitle();
    }

    public String url() {
        return driver.getCurrentUrl();
    }
}
