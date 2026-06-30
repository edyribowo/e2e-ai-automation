@create-user
Feature: Create User (POST /api/users)

  @positive @smoke
  Scenario: TC-CRU-001 Create user with both name and job fields
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"morpheus","job":"leader"}
      """
    Then the response status code should be 201
    And the response body field "name" should equal "morpheus"
    And the response body field "job" should equal "leader"
    And the response body should have a non-empty string field "id"
    And the response body should have a non-empty string field "createdAt"

  @positive
  Scenario: TC-CRU-002 Response id field is a non-empty string
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"morpheus","job":"leader"}
      """
    Then the response status code should be 201
    And the response body should have a non-empty string field "id"

  @positive
  Scenario: TC-CRU-003 Response createdAt is a valid ISO 8601 timestamp
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"morpheus","job":"leader"}
      """
    Then the response status code should be 201
    And the response body field "createdAt" should be a valid ISO 8601 timestamp

  @positive
  Scenario: TC-CRU-004 Create user with name only
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"trinity"}
      """
    Then the response status code should be 201
    And the response body field "name" should equal "trinity"
    And the response body should have a non-empty string field "id"
    And the response body should have a non-empty string field "createdAt"

  @positive
  Scenario: TC-CRU-005 Create user with job only
    When a POST request is sent to "/api/users" with body:
      """
      {"job":"oracle"}
      """
    Then the response status code should be 201
    And the response body field "job" should equal "oracle"
    And the response body should have a non-empty string field "id"
    And the response body should have a non-empty string field "createdAt"

  @edge
  Scenario: TC-CRU-006 Create user with empty JSON body
    When a POST request is sent to "/api/users" with body:
      """
      {}
      """
    Then the response status code should be 201
    And the response body should have a non-empty string field "id"
    And the response body should have a non-empty string field "createdAt"

  @edge
  Scenario: TC-CRU-007 Extra fields are echoed back
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"neo","job":"chosen","age":30,"team":"zion"}
      """
    Then the response status code should be 201
    And the response body field "name" should equal "neo"
    And the response body field "job" should equal "chosen"

  @edge
  Scenario: TC-CRU-008 Create user with special characters in name and job
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"Ñoño & <b>test</b>","job":"dev/ops+lead"}
      """
    Then the response status code should be 201
    And the response body field "name" should equal "Ñoño & <b>test</b>"
    And the response body field "job" should equal "dev/ops+lead"

  @edge
  Scenario: TC-CRU-009 Create user with very long string values
    When a POST request is sent to "/api/users" with a 1000-character name and job
    Then the response status code should be 201
    And the response body should have a non-empty string field "id"
    And the response body should have a non-empty string field "createdAt"

  @negative
  Scenario: TC-CRU-010 Create user — missing x-api-key header
    Given the request has no API key header
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"morpheus","job":"leader"}
      """
    Then the response status code should be 401
    And the response body should contain an auth error

  @negative
  Scenario: TC-CRU-011 Create user — invalid API key
    Given the request uses an invalid API key "bad-key-000"
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"morpheus","job":"leader"}
      """
    Then the response status code should be 401 or 403

  @edge
  Scenario: TC-CRU-012 Create user with numeric values for name and job
    When a POST request is sent to "/api/users" with body:
      """
      {"name":12345,"job":99}
      """
    Then the response status code should be 201
    And the response body should have a non-empty string field "id"
    And the response body should have a non-empty string field "createdAt"

  @positive
  Scenario: TC-CRU-013 Create user — response Content-Type is application/json
    When a POST request is sent to "/api/users" with body:
      """
      {"name":"morpheus","job":"leader"}
      """
    Then the response status code should be 201
    And the response Content-Type header should contain "application/json"
