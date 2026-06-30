@list-users
Feature: List Users Paginated (GET /api/users)

  @positive @smoke
  Scenario: TC-LST-001 Retrieve default first page with no query params
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response body field "page" should equal 1
    And the response body field "per_page" should equal 6
    And the response body "data" array size should be at most 6
    And the response body "total" field should be a positive integer
    And the response body "total_pages" field should be a positive integer

  @positive
  Scenario: TC-LST-002 Retrieve page 2 explicitly
    When a GET request is sent to "/api/users?page=2"
    Then the response status code should be 200
    And the response body field "page" should equal 2
    And the response body "data" array should be non-empty

  @positive
  Scenario: TC-LST-003 Retrieve page 1 explicitly
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And the response body field "page" should equal 1

  @positive
  Scenario: TC-LST-004 Verify response body schema on list
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And the response body should contain fields "page", "per_page", "total", "total_pages", "data", "support"
    And the response body "support" object should contain fields "url", "text"

  @positive
  Scenario: TC-LST-005 Verify each user object schema in data array
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And each user in "data" should have fields "id", "email", "first_name", "last_name", "avatar"

  @positive
  Scenario: TC-LST-006 Custom per_page parameter returns correct count
    When a GET request is sent to "/api/users?per_page=3"
    Then the response status code should be 200
    And the response body field "per_page" should equal 3
    And the response body "data" array size should be at most 3

  @positive
  Scenario: TC-LST-007 total_pages is consistent with total and per_page
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And "total_pages" should equal ceil of "total" divided by "per_page"

  @edge
  Scenario: TC-LST-008 Page beyond last page returns empty data or handled gracefully
    When a GET request is sent to "/api/users?page=9999"
    Then the response status code should not be 500
    And if status is 200 the "data" array should be empty or the response should have a 4xx status

  @edge
  Scenario: TC-LST-009 Page number 0 is handled gracefully
    When a GET request is sent to "/api/users?page=0"
    Then the response status code should not be 500

  @edge
  Scenario: TC-LST-010 Negative page number is handled gracefully
    When a GET request is sent to "/api/users?page=-1"
    Then the response status code should not be 500

  @edge
  Scenario: TC-LST-011 Non-integer page value is handled gracefully
    When a GET request is sent to "/api/users?page=abc"
    Then the response status code should not be 500

  @edge
  Scenario: TC-LST-012 per_page=0 is handled gracefully
    When a GET request is sent to "/api/users?per_page=0"
    Then the response status code should not be 500

  @negative
  Scenario: TC-LST-013 List users — missing x-api-key header
    Given the request has no API key header
    When a GET request is sent to "/api/users"
    Then the response status code should be 401
    And the response body should contain an auth error

  @negative
  Scenario: TC-LST-014 List users — invalid API key
    Given the request uses an invalid API key "wrong-key"
    When a GET request is sent to "/api/users"
    Then the response status code should be 401 or 403

  @positive
  Scenario: TC-LST-015 data array user emails follow valid email format
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And each user's "email" field should match a valid email format

  @positive
  Scenario: TC-LST-016 avatar URLs in data are valid HTTPS URLs
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And each user's "avatar" field should start with "https://"

  @positive
  Scenario: TC-LST-017 Response Content-Type is application/json
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response Content-Type header should contain "application/json"

  @positive
  Scenario: TC-LST-018 Paginated pages together cover all users without overlap
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And I store the user IDs from "data"
    When a GET request is sent to "/api/users?page=2"
    Then the response status code should be 200
    And the user IDs from "data" should not overlap with stored IDs
