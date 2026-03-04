package pages;

import org.openqa.selenium.By;

import core.Waits;

public class CartPage extends BasePage {

    private final By title = By.className("title");
    private final By cartContainer = By.id("cart_contents_container");

    public CartPage assertLoaded() {
        Waits.urlContains(driver, "cart.html");
        Waits.visible(driver, cartContainer);
        String actualTitle = visible(title).getText();
        if (!"Your Cart".equals(actualTitle)) {
            throw new IllegalStateException("Expected title 'Your Cart' but found '" + actualTitle + "'");
        }
    return this;
    }

    public boolean hasItem(String itemName) {
        By item = By.xpath(
                "//div[@id='cart_contents_container']" +
                "//div[contains(@class,'cart_item')]" +
                "[.//div[contains(@class,'inventory_item_name') and normalize-space()='" + itemName + "']]"
        );
        try {
            Waits.visible(driver, item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}