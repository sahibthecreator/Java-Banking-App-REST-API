Feature: Transaction Management

  Scenario: Retrieve transactions with parameters
    Given a TransactionController
    When I call the getTransactions endpoint with parameters
    Then I should receive a response with status 200 and a list of transactions

  Scenario: Retrieve a single transaction by ID
    Given a TransactionController
    When I call the getTransactionById endpoint with transactionId "f619c319-a2ab-41e8-9c4c-e6685263d98f"
    Then I should receive a response with status 200 and a single transaction

  Scenario: Retrieve transactions by userId
    Given a TransactionController
    When I call the getTransactionsByUserId endpoint with userId "8b7bede9-5cb7-4c33-854f-bf5c2028567e"
    Then I should receive a response with status 200 and a list of transactions by userId

  Scenario: Add a transaction
    Given a TransactionController
    When I call the addTransaction endpoint with transaction type "TRANSFER"
    Then I should receive a response with status 201 and the added transaction
