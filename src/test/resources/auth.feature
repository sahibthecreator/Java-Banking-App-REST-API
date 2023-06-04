Feature: Authentication

Scenario: Successful login
  Given I have login request with email "root@gmail.com" and password "11111"
  When I send login request
  Then I receive login response with status code 200
  And I receive valid login response






  Scenario: Successful registration
    Given I have registration request with details
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
    When I send registration request
    Then I receive registration response with status code 201
    And I receive valid registration response
