package pages;

import org.openqa.selenium.By;

import core.Waits;

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
    String slug = toSlug(itemName);

    By addBtn = By.id("add-to-cart-" + slug);
    By removeBtn = By.id("remove-" + slug);

    // already added
    if (!driver.findElements(removeBtn).isEmpty()) return this;

    // attempt 1: normal click
    safeClick(addBtn);

    // if state didn't change quickly, fall back to JS click
    if (!waitForPresent(removeBtn, 2)) {
        jsClick(addBtn);
    }

    // final wait for state transition
    if (!waitForPresent(removeBtn, core.Config.timeoutSeconds())) {
        throw new AssertionError("Add to cart did not complete for: " + itemName +
                " | URL: " + driver.getCurrentUrl());
    }

    return this;
    }

    private String toSlug(String itemName) {
        // "Sauce Labs Backpack" -> "sauce-labs-backpack"
        return itemName.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    private void safeClick(By locator) {
        var el = clickable(locator);
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}
        try {
            el.click();
        } catch (Exception ignored) {
            jsClick(locator);
        }
    }

    private void jsClick(By locator) {
        var el = driver.findElement(locator);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private boolean waitForPresent(By locator, int seconds) {
        try {
            new org.openqa.selenium.support.ui.FluentWait<>(driver)
                    .withTimeout(java.time.Duration.ofSeconds(seconds))
                    .pollingEvery(java.time.Duration.ofMillis(core.Config.pollMillis()))
                    .ignoring(org.openqa.selenium.NoSuchElementException.class)
                    .ignoring(org.openqa.selenium.StaleElementReferenceException.class)
                    .until(d -> !d.findElements(locator).isEmpty());
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    private void clickWithFallback(By locator) {
        var el = clickable(locator);

        // Scroll into view (headless can be finicky)
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}

        try {
            el.click();
        } catch (Exception ignored) {
            jsClick(locator);
        }
    }

    private void jsClick(By locator) {
        var el = driver.findElement(locator);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void waitUntilAdded(By removeBtn) {
        new org.openqa.selenium.support.ui.FluentWait<>(driver)
                .withTimeout(java.time.Duration.ofSeconds(core.Config.timeoutSeconds()))
                .pollingEvery(java.time.Duration.ofMillis(core.Config.pollMillis()))
                .ignoring(org.openqa.selenium.NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class)
                .until(d ->
                        !d.findElements(By.className("shopping_cart_badge")).isEmpty()
                        || !d.findElements(removeBtn).isEmpty()
                );
    }


    public CartPage openCart() {
        for (int attempt = 1; attempt <= 2; attempt++) {
        var el = clickable(cartLink);

        // Ensure it's actually in view (headless can be weird about this)
        try {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}

        // Try regular click
        try {
            el.click();
        } catch (Exception ignored) {
            // We'll try JS click below
        }

        // If we navigated, we’re done
        try {
            core.Waits.urlContains(driver, "cart.html");
            core.Waits.jsReady(driver);
            return new CartPage();
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // still on inventory, try JS click as fallback
        }

        // JS click fallback (often fixes headless flakiness)
        try {
            var el2 = driver.findElement(cartLink);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el2);
        } catch (Exception ignored) {}

        // final wait after JS click
        core.Waits.urlContains(driver, "cart.html");
        core.Waits.jsReady(driver);
        return new CartPage();
    }

    throw new AssertionError("Failed to navigate to cart page. Current URL: " + driver.getCurrentUrl());
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
