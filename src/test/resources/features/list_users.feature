@api @list
Feature: List Users
  As an API client
  I want to list users with pagination via GET /api/users
  So that I can page through the user collection reliably

  Background:
    Given a valid API key is set

  @smoke @positive
  Scenario: TC-LST-001 Retrieve the default first page
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response field "page" should equal 1
    And the response field "per_page" should equal 6
    And the "data" array should contain at most 6 items
    And the response field "total" should be a positive integer
    And the response field "total_pages" should be a positive integer

  @positive
  Scenario: TC-LST-002 Retrieve page 2 explicitly
    When a GET request is sent to "/api/users?page=2"
    Then the response status code should be 200
    And the response field "page" should equal 2
    And the "data" array should not be empty

  @positive
  Scenario: TC-LST-003 Retrieve page 1 explicitly
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And the response field "page" should equal 1

  @positive
  Scenario: TC-LST-004 List response matches the expected schema
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And the response should have the integer fields "page, per_page, total, total_pages"
    And the response field "data" should be an array
    And the response should have a "support" object with fields "url, text"

  @positive
  Scenario: TC-LST-005 Each user object matches the expected schema
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And each user in "data" should have an integer "id"
    And each user in "data" should have a valid email "email"
    And each user in "data" should have non-empty "first_name" and "last_name"
    And each user in "data" should have a valid URL "avatar"
    And each user in "data" maps to the User schema

  @positive
  Scenario: TC-LST-006 Custom per_page returns the correct count
    When a GET request is sent to "/api/users?per_page=3"
    Then the response status code should be 200
    And the response field "per_page" should equal 3
    And the "data" array should contain at most 3 items
    And the total_pages should equal the ceiling of total divided by per_page

  @positive
  Scenario: TC-LST-007 total_pages is consistent with total and per_page
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the total_pages should equal the ceiling of total divided by per_page

  @edge
  Scenario Outline: TC-LST-008/009/010/011/012 Pagination edge cases are handled gracefully
    When a GET request is sent to "/api/users?<query>"
    Then the response status code should be one of "200, 400"
    And the response should not be a server error

    Examples:
      | query      |
      | page=9999  |
      | page=0     |
      | page=-1    |
      | page=abc   |
      | per_page=0 |

  @positive
  Scenario: TC-LST-015 User emails follow a valid email format
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And each user in "data" should have a valid email "email"

  @positive
  Scenario: TC-LST-016 Avatar values are well-formed https URLs
    When a GET request is sent to "/api/users?page=1"
    Then the response status code should be 200
    And each user in "data" should have an "avatar" URL starting with "https://"

  @positive
  Scenario: TC-LST-017 Response Content-Type is JSON
    When a GET request is sent to "/api/users"
    Then the response status code should be 200
    And the response Content-Type should be JSON

  @positive
  Scenario: TC-LST-018 Pages cover all users without overlap
    When the first two pages of "/api/users" are fetched
    Then there should be no duplicate user ids across the two pages

  @negative
  Scenario: TC-LST-013 List users without an API key is rejected
    Given no API key header is set
    When a GET request is sent to "/api/users"
    Then the response status code should be 401
    And the response field "error" should equal "Missing API key."
    And the response body should contain the field "how_to_get_one"

  @negative
  Scenario: TC-LST-014 List users with an invalid API key is rejected
    Given the API key is set to "wrong-key"
    When a GET request is sent to "/api/users"
    Then the response status code should be 401
    And the response should contain an error message
