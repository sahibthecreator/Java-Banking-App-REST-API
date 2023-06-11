Feature: Account Management

  Scenario: Retrieve all accounts
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then The response status should be 200
    And The response should be a list of accounts

#    This scenario works when the AccountDTO Json properties set to READ_ONLY
#    are changed to READ_WRITE to process the data in the When step
  Scenario: Create an account
    Given The endpoint for "accounts" is available for method "POST"
    When I send a POST request to the "accounts" endpoint with the account details
    """
    {
    "id": "918bc3d0-c648-4547-123d-b036b0be119e",
	"iban": "NL00INHO521927408",
	"balance": 0.0,
    "typeOfAccount": "CURRENT",
	"userId": "46abae22-78f8-441a-ad4e-a352ceeadd01",
	"dateOfOpening": "10-06-2023",
	"absoluteLimit": 0.0,
	"active": true
    }
    """
    Then A new account should be created

