Feature: User API

  Scenario: Get all users
    Given When the endpoint "users" is available for method "GET"
    When I retrieve all users
    Then the response should have status code 200
    And the response should contain 1 users
