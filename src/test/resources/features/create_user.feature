@api @create
Feature: Create User
  As an API client
  I want to create users via POST /api/users
  So that the service echoes my payload with a generated id and timestamp

  Background:
    Given a valid API key is set

  @smoke @positive
  Scenario: TC-CRU-001 Create a user with name and job
    Given a user payload with name "morpheus" and job "leader"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "name" should equal "morpheus"
    And the response field "job" should equal "leader"
    And the response field "id" should be a non-empty string
    And the response field "createdAt" should be a valid ISO 8601 timestamp

  @positive
  Scenario: TC-CRU-002 Created user id is a non-empty string
    Given a user payload with name "morpheus" and job "leader"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "id" should be a non-empty string

  @positive
  Scenario: TC-CRU-003 createdAt is a recent ISO 8601 timestamp
    Given a user payload with name "morpheus" and job "leader"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "createdAt" should be a valid ISO 8601 timestamp
    And the "createdAt" timestamp should be within 120 seconds of now

  @positive
  Scenario: TC-CRU-004 Create a user with name only
    Given a user payload with only name "trinity"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "name" should equal "trinity"
    And the response field "id" should be a non-empty string
    And the response field "createdAt" should be a valid ISO 8601 timestamp

  @positive
  Scenario: TC-CRU-005 Create a user with job only
    Given a user payload with only job "oracle"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "job" should equal "oracle"
    And the response field "id" should be a non-empty string
    And the response field "createdAt" should be a valid ISO 8601 timestamp

  @edge
  Scenario: TC-CRU-006 Create a user with an empty JSON body
    Given an empty JSON payload
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "id" should be a non-empty string
    And the response field "createdAt" should be a valid ISO 8601 timestamp

  @edge
  Scenario: TC-CRU-007 Extra/unexpected fields are echoed back
    Given a user payload with the following fields:
      | name | neo    |
      | job  | chosen |
      | age  | 30     |
      | team | zion   |
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "name" should equal "neo"
    And the response field "job" should equal "chosen"
    And the response field "age" should equal "30"
    And the response field "team" should equal "zion"

  @edge
  Scenario: TC-CRU-008 Special characters are echoed verbatim
    Given a user payload with name "Ñoño & <b>test</b>" and job "dev/ops+lead"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "name" should equal "Ñoño & <b>test</b>"
    And the response field "job" should equal "dev/ops+lead"

  @edge
  Scenario: TC-CRU-009 Very long string values are accepted
    Given a user payload with a 1000-character name and a 1000-character job
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "name" should have length 1000
    And the response field "job" should have length 1000
    And the response field "id" should be a non-empty string

  @edge
  Scenario: TC-CRU-012 Numeric values for name and job are echoed back
    Given a user payload with numeric name 12345 and numeric job 99
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response field "name" should equal "12345"
    And the response field "job" should equal "99"
    And the response field "id" should be a non-empty string

  @positive
  Scenario: TC-CRU-013 Response Content-Type is JSON
    Given a user payload with name "morpheus" and job "leader"
    When a POST request is sent to "/api/users"
    Then the response status code should be 201
    And the response Content-Type should be JSON

  @negative
  Scenario: TC-CRU-010 Create user without an API key is rejected
    Given no API key header is set
    And a user payload with name "morpheus" and job "leader"
    When a POST request is sent to "/api/users"
    Then the response status code should be 401
    And the response field "error" should equal "Missing API key."

  @negative
  Scenario: TC-CRU-011 Create user with an invalid API key is rejected
    Given the API key is set to "bad-key-000"
    And a user payload with name "morpheus" and job "leader"
    When a POST request is sent to "/api/users"
    Then the response status code should be 401
    And the response should contain an error message
