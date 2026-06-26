@api @cross-cutting
Feature: Cross-Cutting Concerns
  As a quality engineer
  I want to verify non-functional and contract-level behaviors
  So that the API is robust beyond its happy paths

  Background:
    Given a valid API key is set

  @positive
  Scenario: TC-X-001 A created user does not appear in subsequent GET lists (mock behavior)
    Given a user payload with name "agent-smith" and job "agent"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the created user id should not appear in the first two pages of "/api/users"

  @nonfunctional
  Scenario: TC-X-002 The API responds within acceptable latency
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response time should be under 3000 ms

  @negative
  Scenario: TC-X-003 An unsupported HTTP method returns a client error
    When a DELETE request is sent to "/api/users"
    Then the response status code should be one of "404, 405"
    And the response should not be a server error

  @edge
  Scenario: TC-X-004 POST without a Content-Type header is handled gracefully
    Given a user payload with name "morpheus" and job "leader"
    When a POST request is sent to "/api/users" without a Content-Type header
    Then the response status code should be one of "201, 400"
    And the response should not be a server error

  @negative
  Scenario: TC-X-005 Malformed JSON body is handled gracefully
    Given a raw request body "{name: morpheus}"
    When a POST request is sent to "/api/users"
    Then the response status code should be one of "201, 400"
    And the response should not be a server error
