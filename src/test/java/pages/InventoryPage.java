package pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.openqa.selenium.By;

import core.Waits;

public class InventoryPage extends BasePage {

    private final By title = By.className("title");
    private final By inventoryContainer = By.id("inventory_container");

    public InventoryPage assertLoaded() {
        Waits.visible(driver, inventoryContainer);
        assertEquals("Products", visible(title).getText(), "Inventory page title mismatch");
        return this;
    }
}