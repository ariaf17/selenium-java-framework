package pages;

import core.Waits;
import org.openqa.selenium.By;

public class InventoryPage extends BasePage {

    private final By title = By.className("title");
    private final By inventoryContainer = By.id("inventory_container");
    private final By cartLink = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");

    public InventoryPage assertLoaded() {
        Waits.visible(driver, inventoryContainer);
        String actualTitle = visible(title).getText();
        if (!"Products".equals(actualTitle)) {
            throw new AssertionError("Expected title 'Products' but was: " + actualTitle);
        }
        return this;
    }

    /**
     * Adds an item by its display name (e.g. "Sauce Labs Backpack").
     * Locates the matching inventory item container, then clicks its add-to-cart button.
     */
    public InventoryPage addItemToCart(String itemName) {
        By addBtn = By.xpath(
                "//div[contains(@class,'inventory_item')]" +
                "[.//div[contains(@class,'inventory_item_name') and normalize-space()='" + escapeXPath(itemName) + "']]" +
                "//button[contains(@id,'add-to-cart') or contains(@data-test,'add-to-cart')]"
        );
        click(addBtn);
        return this;
    }

    public CartPage openCart() {
        click(cartLink);
        return new CartPage();
    }

    public int cartBadgeCount() {
        try {
            String text = driver.findElement(cartBadge).getText();
            return Integer.parseInt(text.trim());
        } catch (Exception ignored) {
            return 0; // badge not present when empty
        }
    }

    // Minimal XPath string escaping (handles single quotes)
    private String escapeXPath(String input) {
        if (!input.contains("'")) return input;
        // concat('a', "'", 'b')
        String[] parts = input.split("'");
        StringBuilder sb = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            sb.append("'").append(parts[i]).append("'");
            if (i < parts.length - 1) sb.append(", \"'\", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
