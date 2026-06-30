# Test Cases — ReqRes Users API

**Service:** ReqRes Mock REST API  
**Base URL:** `https://reqres.in/api`  
**Version:** v1  
**Content-Type:** `application/json`  
**Generated:** 2026-06-26  

---

## Section 1 — Authentication (Global)

| TC ID | Title | Type | Pre-conditions | Test Steps | Test Data | Expected Result |
|-------|-------|------|----------------|------------|-----------|-----------------|
| TC-AUTH-001 | Request with valid API key is accepted | Positive | API key `reqres-free-v1` is known | 1. Send `GET /api/users` with header `x-api-key: reqres-free-v1` | `x-api-key: reqres-free-v1` | HTTP 200 OK; response body contains `page`, `data`, `total` fields |
| TC-AUTH-002 | Request without API key header is rejected | Negative | No API key header is set | 1. Send `GET /api/users` with **no** `x-api-key` header | _(no header)_ | HTTP 401; response body contains `"error": "Missing API key."` and `"how_to_get_one"` field |
| TC-AUTH-003 | Request with invalid/wrong API key is rejected | Negative | Any non-valid API key string | 1. Send `GET /api/users` with `x-api-key: invalid-key-xyz` | `x-api-key: invalid-key-xyz` | HTTP 401; response body contains an error message |
| TC-AUTH-004 | Request with empty string API key is rejected | Edge Case | — | 1. Send `GET /api/users` with `x-api-key: ` (empty value) | `x-api-key: ""` | HTTP 401; error response returned |

---

## Section 2 — Create User (`POST /api/users`)

| TC ID | Title | Type | Pre-conditions | Test Steps | Test Data | Expected Result |
|-------|-------|------|----------------|------------|-----------|-----------------|
| TC-CRU-001 | Create user with both `name` and `job` fields | Positive | Valid API key available | 1. Send `POST /api/users` with valid headers and body `{"name":"morpheus","job":"leader"}` | `{"name": "morpheus", "job": "leader"}` | HTTP 201 Created; response body echoes `name` and `job`; contains server-generated `id` (non-empty string) and `createdAt` (ISO 8601 timestamp) |
| TC-CRU-002 | Response `id` field is a non-empty string | Positive | TC-CRU-001 pre-conditions | 1. Execute TC-CRU-001 steps | Same as TC-CRU-001 | `id` field in response is of type `string` and is not empty or null |
| TC-CRU-003 | Response `createdAt` is a valid ISO 8601 timestamp | Positive | TC-CRU-001 pre-conditions | 1. Execute TC-CRU-001 steps; 2. Parse `createdAt` value | Same as TC-CRU-001 | `createdAt` matches ISO 8601 format (e.g., `2025-06-26T09:12:44.114Z`); timestamp is recent (within a reasonable delta of current UTC time) |
| TC-CRU-004 | Create user with `name` only (no `job`) | Positive | Valid API key available | 1. Send `POST /api/users` with body `{"name":"trinity"}` | `{"name": "trinity"}` | HTTP 201 Created; response echoes `name`; `id` and `createdAt` are present; `job` is absent or null |
| TC-CRU-005 | Create user with `job` only (no `name`) | Positive | Valid API key available | 1. Send `POST /api/users` with body `{"job":"oracle"}` | `{"job": "oracle"}` | HTTP 201 Created; response echoes `job`; `id` and `createdAt` are present; `name` is absent or null |
| TC-CRU-006 | Create user with empty JSON body `{}` | Edge Case | Valid API key available | 1. Send `POST /api/users` with empty body `{}` | `{}` | HTTP 201 Created; response contains `id` and `createdAt`; no `name` or `job` fields (or both null/absent) |
| TC-CRU-007 | Extra/unexpected fields are echoed back | Edge Case | Valid API key available | 1. Send `POST /api/users` with extra fields in body | `{"name": "neo", "job": "chosen", "age": 30, "team": "zion"}` | HTTP 201 Created; all sent fields (`name`, `job`, `age`, `team`) are echoed back; `id` and `createdAt` present |
| TC-CRU-008 | Create user with special characters in `name` and `job` | Edge Case | Valid API key available | 1. Send `POST /api/users` with special character values | `{"name": "Ñoño & <b>test</b>", "job": "dev/ops+lead"}` | HTTP 201 Created; special characters are echoed back verbatim (no sanitization alters the echoed value) |
| TC-CRU-009 | Create user with very long string values | Edge Case | Valid API key available | 1. Send `POST /api/users` with 1000-character strings for both fields | `{"name": "a"*1000, "job": "b"*1000}` | HTTP 201 Created; values are echoed back; `id` and `createdAt` present |
| TC-CRU-010 | Create user — missing `x-api-key` header | Negative | — | 1. Send `POST /api/users` with valid body but **no** `x-api-key` header | `{"name": "morpheus", "job": "leader"}` (no API key) | HTTP 401; body contains `"error": "Missing API key."` |
| TC-CRU-011 | Create user — invalid API key | Negative | — | 1. Send `POST /api/users` with invalid API key | `x-api-key: bad-key-000` | HTTP 401; error response returned |
| TC-CRU-012 | Create user with numeric values for `name` and `job` | Edge Case | Valid API key available | 1. Send `POST /api/users` with integer values for string fields | `{"name": 12345, "job": 99}` | HTTP 201 Created; values echoed back (API does not enforce types); `id` and `createdAt` present |
| TC-CRU-013 | Create user — response `Content-Type` is `application/json` | Positive | Valid API key available | 1. Execute TC-CRU-001; 2. Inspect response headers | Same as TC-CRU-001 | Response header `Content-Type` is `application/json` or `application/json; charset=utf-8` |

---

## Section 3 — List Users Paginated (`GET /api/users`)

| TC ID | Title | Type | Pre-conditions | Test Steps | Test Data | Expected Result |
|-------|-------|------|----------------|------------|-----------|-----------------|
| TC-LST-001 | Retrieve default first page (no query params) | Positive | Valid API key available | 1. Send `GET /api/users` with only the `x-api-key` header | _(no query params)_ | HTTP 200 OK; `page` = 1; `per_page` = 6; `data` array contains ≤ 6 user objects; `total` and `total_pages` are positive integers |
| TC-LST-002 | Retrieve page 2 explicitly | Positive | Valid API key available | 1. Send `GET /api/users?page=2` | `page=2` | HTTP 200 OK; `page` = 2; `data` array is non-empty; user objects on page 2 differ from page 1 |
| TC-LST-003 | Retrieve page 1 explicitly | Positive | Valid API key available | 1. Send `GET /api/users?page=1` | `page=1` | HTTP 200 OK; `page` = 1; result matches default (no-param) call |
| TC-LST-004 | Verify response body schema on list | Positive | Valid API key available | 1. Send `GET /api/users?page=1`; 2. Validate all fields | `page=1` | Response contains: `page` (integer), `per_page` (integer), `total` (integer), `total_pages` (integer), `data` (array), `support` (object with `url` and `text`) |
| TC-LST-005 | Verify each user object schema in `data` array | Positive | Valid API key available | 1. Send `GET /api/users?page=1`; 2. Iterate over `data` array | `page=1` | Each object in `data` contains: `id` (integer), `email` (valid email format), `first_name` (string), `last_name` (string), `avatar` (valid URL string) |
| TC-LST-006 | Custom `per_page` parameter returns correct count | Positive | Valid API key available | 1. Send `GET /api/users?per_page=3` | `per_page=3` | HTTP 200 OK; `per_page` = 3; `data` array contains ≤ 3 items; `total_pages` = `ceil(total / 3)` |
| TC-LST-007 | `total_pages` is consistent with `total` and `per_page` | Positive | Valid API key available | 1. Send `GET /api/users`; 2. Compute `ceil(total / per_page)` | _(no query params)_ | `total_pages` in response equals `ceil(total / per_page)` |
| TC-LST-008 | Page beyond last page returns empty `data` or handled gracefully | Edge Case | Valid API key available | 1. Send `GET /api/users?page=9999` | `page=9999` | HTTP 200 OK; `data` array is empty `[]` **OR** API returns a meaningful error; no server 500 error |
| TC-LST-009 | Page number `0` is handled gracefully | Edge Case | Valid API key available | 1. Send `GET /api/users?page=0` | `page=0` | HTTP 200 or 400; no server 500 error; if 200, behavior is documented/consistent |
| TC-LST-010 | Negative page number is handled gracefully | Edge Case | Valid API key available | 1. Send `GET /api/users?page=-1` | `page=-1` | HTTP 200 or 400; no server 500 error; response is consistent |
| TC-LST-011 | Non-integer page value is handled gracefully | Edge Case | Valid API key available | 1. Send `GET /api/users?page=abc` | `page=abc` | HTTP 200 or 400; no server 500 error; if 200, defaults to page 1 or returns an error message |
| TC-LST-012 | `per_page=0` is handled gracefully | Edge Case | Valid API key available | 1. Send `GET /api/users?per_page=0` | `per_page=0` | HTTP 200 or 400; no server 500 error; response is consistent |
| TC-LST-013 | List users — missing `x-api-key` header | Negative | — | 1. Send `GET /api/users` with **no** `x-api-key` header | _(no API key)_ | HTTP 401; body contains `"error": "Missing API key."` and `"how_to_get_one"` |
| TC-LST-014 | List users — invalid API key | Negative | — | 1. Send `GET /api/users` with `x-api-key: wrong-key` | `x-api-key: wrong-key` | HTTP 401; error response returned |
| TC-LST-015 | `data` array user emails follow valid email format | Positive | Valid API key available | 1. Send `GET /api/users?page=1`; 2. Validate each `email` field with regex | `page=1` | All `email` values match a standard email format (e.g., `user@domain.tld`) |
| TC-LST-016 | `avatar` URLs in `data` are reachable/valid URLs | Positive | Valid API key available | 1. Send `GET /api/users?page=1`; 2. Validate `avatar` field per user | `page=1` | All `avatar` values are well-formed URLs beginning with `https://` |
| TC-LST-017 | Response `Content-Type` is `application/json` | Positive | Valid API key available | 1. Send `GET /api/users`; 2. Inspect response headers | _(no query params)_ | Response header `Content-Type` is `application/json` or `application/json; charset=utf-8` |
| TC-LST-018 | Paginated pages together cover all users without overlap | Positive | Valid API key; ≥ 2 total pages | 1. Fetch page 1; collect all `id` values; 2. Fetch page 2; collect all `id` values; 3. Verify no duplicates | `page=1`, then `page=2` | Combined `id` count = `total`; no `id` appears on both pages |

---

## Section 4 — Cross-Cutting Concerns

| TC ID | Title | Type | Pre-conditions | Test Steps | Test Data | Expected Result |
|-------|-------|------|----------------|------------|-----------|-----------------|
| TC-X-001 | POST-created user does not appear in GET list (mock behavior) | Positive | Valid API key available | 1. `POST /api/users` with `{"name":"agent-smith","job":"agent"}`; 2. `GET /api/users?page=1` and page 2; 3. Search for the new `id` in `data` | `{"name": "agent-smith", "job": "agent"}` | The newly created user's `id` is **not** found in subsequent GET responses — confirming mock/non-persistent behavior |
| TC-X-002 | API responds within acceptable latency | Non-functional | Valid API key available | 1. Send `GET /api/users`; 2. Measure response time | _(standard request)_ | Response received within 3 000 ms (adjust threshold per SLA) |
| TC-X-003 | Unsupported HTTP method returns appropriate error | Negative | Valid API key available | 1. Send `DELETE /api/users` (if not in contract) | `x-api-key: reqres-free-v1` | HTTP 404 or 405 Method Not Allowed; no server 500 error |
| TC-X-004 | Request with no `Content-Type` header on POST | Edge Case | Valid API key available | 1. Send `POST /api/users` body without `Content-Type` header | `{"name": "morpheus", "job": "leader"}` (no Content-Type) | HTTP 201 or 400; no server 500 error; behavior is documented/consistent |
| TC-X-005 | Malformed JSON body on POST is handled gracefully | Negative | Valid API key available | 1. Send `POST /api/users` with malformed JSON string as body | `{name: morpheus}` (invalid JSON) | HTTP 400 Bad Request or 201 (if API ignores body); no server 500 error |
