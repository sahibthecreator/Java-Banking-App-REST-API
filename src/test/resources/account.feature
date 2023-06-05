Feature: Account Management

  Scenario: Retrieve all accounts
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then The response status should be 200
    And The response should be a list of accounts

  Scenario: Create an account
    Given The endpoint for "accounts" is available for method "POST"
    And I have a request with account details
    """
    {
      "userId": "12345678-1234-1234-1234-1234567890ab",
      "iban": "NL00INHO1000000001",
      "typeOfAccount": "SAVINGS",
      "dateOfOpening": "2018-11-30",
      "balance": "0.0",
      "active": "true"
    }
    """
    When I send a POST request to the "/accounts" endpoint with the account details
    Then A new account should be created


  Scenario: Retrieve Account Info by IBAN
    Given The endpoint for "accounts/NL00INHO1000000001/accountInfo" is available for method "GET"
    When I retrieve the account info for IBAN "NL00INHO1000000001"
    Then The response status should be 200
    And The response body should contain the account details

  Scenario: Retrieve IBANs by Customer Name
    Given The endpoint for "accounts/iban" is available for method "GET"
    When I retrieve IBANs by customer name with the following details:
      | firstname | lastname |
      | John      | Doe      |
    Then The response status should be 200
    And The response body should contain a list of CustomerIbanDTOs

  Scenario: Deactivate Account
    Given The endpoint for "accounts/NL00INHO1000000001" is available for method "PATCH"
    When I deactivate the account with IBAN "NL00INHO1000000001"
    Then The response status fro deactivating account should be 200
    And The response body should contain a success message

