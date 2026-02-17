package bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CartPage;
import pages.InventoryPage;

public class CartSteps {

    private CartPage cartPage;

    @When("I add item {string} to the cart")
    public void iAddItemToTheCart(String itemName) {
        // Works even if LoginSteps performed login, because pages use DriverManager internally
        new InventoryPage()
                .assertLoaded()
                .addItemToCart(itemName);
    }

    @And("I open the cart")
    public void iOpenTheCart() {
        cartPage = new InventoryPage()
                .assertLoaded()
                .openCart()
                .assertLoaded();
    }

    @Then("I should see {string} in the cart")
    public void iShouldSeeInTheCart(String itemName) {
        if (!cartPage.hasItem(itemName)) {
            throw new AssertionError("Expected to find item in cart: " + itemName);
        }
    }
}