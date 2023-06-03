Feature: Accounts API

  Scenario: Getting all accounts
    Given The login endpoint is available
    When I send a GET request to "accounts" with the bearer token
    Then I should get a 200 status code
    Then Get a list of all accounts

  Scenario: Posting an account
    Given I perform a POST request to the endpoint "accounts" with the following data:
      | iban | balance | typeOfAccount | userId | dateOfOpening | absoluteLimit |
      | NL01INHO1020304050 | 1000 | SAVINGS | 1 | 2020-01-01 | 1000 |
    When I post the account
    Then I should retrieve the posted account