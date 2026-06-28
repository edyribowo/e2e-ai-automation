@api @auth
Feature: Authentication
  As a consumer of the ReqRes Users API
  I want requests to be authenticated with an API key
  So that only authorized clients can access the service

  @smoke @positive
  Scenario: TC-AUTH-001 Request with a valid API key is accepted
    Given a valid API key is set
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response body should contain the fields "page, data, total"

  @negative
  Scenario: TC-AUTH-002 Request without an API key is rejected
    Given no API key header is set
    When a GET request is sent to "/api/users"
    Then the response status code should be 401
    And the response field "error" should equal "missing_api_key"

  @negative @edge
  Scenario Outline: TC-AUTH-003/004 Request with an invalid or empty API key is rejected
    Given the API key is set to "<apiKey>"
    When a GET request is sent to "/api/users"
    Then the response status code should be one of "401, 403"
    And the response should contain an error message

    Examples:
      | apiKey          |
      | invalid-key-xyz |
      |                 |
