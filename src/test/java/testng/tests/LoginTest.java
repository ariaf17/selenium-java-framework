package testng.tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.InventoryPage;
import pages.LoginPage;
import testng.base.BaseTest;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "auth"})
    public void successfulLogin_standardUser() {
        LoginPage loginPage = new LoginPage().assertLoaded();
        InventoryPage inventoryPage = loginPage.loginAs("standard_user", "secret_sauce");
        inventoryPage.assertLoaded();
    }

    @Test(groups = {"negative", "auth"}, dataProvider = "invalidUsers")
    public void loginFails_forInvalidOrLockedUsers(String username, String password, String expectedMsgFragment) {
        LoginPage loginPage = new LoginPage().assertLoaded();
        loginPage.loginExpectingFailure(username, password);

        String msg = loginPage.errorMessage().toLowerCase();
        if (!msg.contains(expectedMsgFragment.toLowerCase())) {
            throw new AssertionError("Expected error to contain '" + expectedMsgFragment + "' but was: " + msg);
        }
    }

    @DataProvider
    public Object[][] invalidUsers() {
        return new Object[][]{
                {"locked_out_user", "secret_sauce", "locked out"},
                {"standard_user", "wrong_password", "username and password"},
                {"", "secret_sauce", "username"},
                {"standard_user", "", "password"}
        };
    }
}