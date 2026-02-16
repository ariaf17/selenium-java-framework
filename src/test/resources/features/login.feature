@smoke @auth
Feature: Login

  Scenario: Successful login with a standard user
    Given I am on the login page
    When I log in with username "standard_user" and password "secret_sauce"
    Then I should see the inventory page

  @negative
  Scenario: Locked out user cannot log in
    Given I am on the login page
    When I attempt to log in with username "locked_out_user" and password "secret_sauce"
    Then I should see a login error containing "locked out"