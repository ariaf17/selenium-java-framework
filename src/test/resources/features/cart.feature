@smoke @regression @cart
Feature: Cart

  Scenario: Add an item to cart and see it in the cart page
    Given I am on the login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the inventory page
    When I add item "Sauce Labs Backpack" to the cart
    And I open the cart
    Then I should see "Sauce Labs Backpack" in the cart

    Scenario: Remove an item from the cart and see it removed in the cart page
    Given I am on the login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the inventory page
    When I add item "Sauce Labs Backpack" to the cart   