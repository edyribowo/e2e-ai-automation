@cross-cutting
Feature: Cross-Cutting Concerns

  @positive
  Scenario: TC-X-001 POST-created user does not appear in GET list (mock behavior)
    Given the request uses a valid API key
    And the request content type is "application/json"
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"agent-smith","job":"agent"}
      """
    Then the response status code should be 201
    And I store the created user id from the response
    Given the request uses a valid API key
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And the stored user id should not appear in the "data" array
    When a GET request is sent to "/api/users?page=2"
    Then the stored user id should not appear in the "data" array

  @positive
  Scenario: TC-X-002 API responds within acceptable latency
    Given the request uses a valid API key
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response time should be less than 3000 milliseconds

  @edge
  Scenario: TC-X-003 Unsupported HTTP method returns appropriate error
    Given the request uses a valid API key
    When a DELETE request is sent to "/api/users"
    Then the response status code should be 404 or 405

  @edge
  Scenario: TC-X-004 Request with no Content-Type header on POST
    Given the request uses a valid API key
    And the request has no Content-Type header
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"morpheus","job":"leader"}
      """
    Then the response status code should not be 500

  @edge
  Scenario: TC-X-005 Malformed JSON body on POST is handled gracefully
    Given the request uses a valid API key
    And the request content type is "application/json"
    When a POST request is sent to "/api/users" with raw body "{name: morpheus}"
    Then the response status code should not be 500
