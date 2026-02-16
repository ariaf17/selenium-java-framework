package bdd.steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.InventoryPage;
import pages.LoginPage;

public class LoginSteps {

    private final LoginPage loginPage = new LoginPage();
    private InventoryPage inventoryPage;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage.assertLoaded();
    }

    @When("I log in with username {string} and password {string}")
    public void iLogInWithUsernameAndPassword(String username, String password) {
        inventoryPage = loginPage.loginAs(username, password);
    }

    @Then("I should see the inventory page")
    public void iShouldSeeTheInventoryPage() {
        inventoryPage.assertLoaded();
    }

    @When("I attempt to log in with username {string} and password {string}")
    public void iAttemptToLogInWithUsernameAndPassword(String username, String password) {
        loginPage.loginExpectingFailure(username, password);
    }

    @Then("I should see a login error containing {string}")
    public void iShouldSeeALoginErrorContaining(String expectedFragment) {
        String msg = loginPage.errorMessage();
        assertTrue(msg.toLowerCase().contains(expectedFragment.toLowerCase()),
                "Expected error to contain '" + expectedFragment + "' but was: " + msg);
    }
}