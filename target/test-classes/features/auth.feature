Feature: Authentication (Global)
  All API endpoints require a valid x-api-key header.

  @positive @smoke
  Scenario: TC-AUTH-001 Request with valid API key is accepted
    Given the request uses a valid API key
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response body should contain fields "page", "data", "total"

  @negative @auth
  Scenario: TC-AUTH-002 Request without API key header is rejected
    Given the request has no API key header
    When a GET request is sent to "/api/users"
    Then the response status code should be 401
    And the response body should contain an auth error

  @negative @auth
  Scenario: TC-AUTH-003 Request with invalid API key is rejected
    Given the request uses an invalid API key "invalid-key-xyz"
    When a GET request is sent to "/api/users"
    Then the response status code should be 401 or 403

  @negative @auth
  Scenario: TC-AUTH-004 Request with empty string API key is rejected
    Given the request uses an invalid API key ""
    When a GET request is sent to "/api/users"
    Then the response status code should be 401 or 403
