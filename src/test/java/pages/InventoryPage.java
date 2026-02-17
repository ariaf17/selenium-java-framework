package pages;

import org.openqa.selenium.By;

import core.Waits;

public class InventoryPage extends BasePage {

    private final By title = By.className("title");
    private final By inventoryContainer = By.id("inventory_container");

    public InventoryPage assertLoaded() {
        Waits.visible(driver, inventoryContainer);
        String actualTitle = visible(title).getText();
        if (!"Products".equals(actualTitle)) {
            throw new AssertionError("Expected title 'Products' but was: " + actualTitle);
        }
        return this;
    }
}
