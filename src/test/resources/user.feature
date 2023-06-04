Feature: User Management

  Scenario: Get all users
    Given When the endpoint "users" is available for method "GET"
    When I retrieve all users
    Then the response should have status code 200
    And the response should contain 1 users

  Scenario: Create a new user
    Given When the endpoint "/users" is available for method "POST"
    When I request a creation of user with details
    """
  {
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password1",
  "bsn": "569123041",
  "dateOfBirth": "23-03-2004"
  }
    """
    Then the user should be created successfully

  Scenario: Get user by ID
    Given When the endpoint "/users" is available for method "POST"
    When I request a creation of user with details
    """
  {
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password1",
  "bsn": "569123041",
  "dateOfBirth": "23-03-2004"
  }
    """
    Then the user should be created successfully
    Given When the endpoint "/users/{userId}" is available for method "GET" with userId
    When I retrieve the user by ID
    Then the user with ID should exist

  Scenario: Update user details
    Given When the endpoint "/users" is available for method "POST"
    When I request a creation of user with details
    """
  {
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password1",
  "bsn": "569123041",
  "dateOfBirth": "23-03-2004"
  }
    """
    Then the user should be created successfully
    Given When the endpoint "/users/{userId}" is available for method "PUT" with userId
    When I update the user details
    """
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "bsn": "569123041",
      "dateOfBirth": "20-04-2004"
    }
    """
    Then the user details should be updated


  Scenario: Delete user
    Given When the endpoint "/users" is available for method "POST"
    When I request a creation of user with details
    """
  {
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password1",
  "bsn": "569123041",
  "dateOfBirth": "23-03-2004"
  }
    """
    Then the user should be created successfully
    Given When the endpoint "/users/{userId}" is available for method "DELETE" with userId
    When I delete the user
    Then the user should be deleted