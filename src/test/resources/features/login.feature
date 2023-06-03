Feature: Login

  Scenario: Successful login
    Given The login endpoint is available
    When I send a POST request to "/auth/login" with the following credentials:
      | email | password |
      | root@gmail.com  | 11111 |
    Then The response status code should be 200
    And The response body should contain a user id {string} and a JWT token {string}