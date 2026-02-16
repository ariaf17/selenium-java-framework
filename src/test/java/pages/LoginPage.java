package pages;

import core.Waits;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage extends BasePage {

    private final By username = By.id("user-name");
    private final By password = By.id("password");
    private final By loginBtn = By.id("login-button");
    private final By error = By.cssSelector("h3[data-test='error']");

    public LoginPage assertLoaded() {
        Waits.visible(driver, username);
        assertTrue(driver.getCurrentUrl().contains("saucedemo.com"), "Not on SauceDemo domain");
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