package pages;

import org.openqa.selenium.By;

import core.Waits;

public class LoginPage extends BasePage {

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginBtn = By.id("login-button");
    private final By error = By.cssSelector("h3[data-test='error']");

    public LoginPage assertLoaded() {
        Waits.visible(driver, username);
        String url = driver.getCurrentUrl();
        if (!url.contains("saucedemo.com")) {
            throw new AssertionError("Expected SauceDemo domain but was: " + url);
        }
        return this;
    }

    public InventoryPage loginAs(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginBtn);
        return new InventoryPage();
    }

    public LoginPage loginExpectingFailure(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginBtn);
        return this;
    }

    public String errorMessage() {
        return visible(error).getText();
    }
}
