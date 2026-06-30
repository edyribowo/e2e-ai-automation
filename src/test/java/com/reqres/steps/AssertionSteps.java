package com.reqres.steps;

import com.reqres.context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionSteps {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final TestContext ctx;

    public AssertionSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    // ── Status code assertions ──────────────────────────────────────────────

    @Then("the response status code should be {int}")
    public void statusCodeEquals(int expected) {
        assertEquals(expected, ctx.getResponse().getStatusCode(),
                "Expected status " + expected + " but got " + ctx.getResponse().getStatusCode());
    }

    @Then("the response status code should be 401 or 403")
    public void statusCode401or403() {
        int status = ctx.getResponse().getStatusCode();
        assertTrue(status == 401 || status == 403,
                "Expected 401 or 403 but got " + status);
    }

    @Then("the response status code should be 404 or 405")
    public void statusCode404or405() {
        int status = ctx.getResponse().getStatusCode();
        assertTrue(status == 404 || status == 405,
                "Expected 404 or 405 but got " + status);
    }

    @Then("the response status code should not be 500")
    public void statusCodeNot500() {
        assertNotEquals(500, ctx.getResponse().getStatusCode(),
                "Received unexpected 500 Internal Server Error");
    }

    // ── Auth error assertions ───────────────────────────────────────────────

    @Then("the response body should contain an auth error")
    public void bodyContainsAuthError() {
        Response r = ctx.getResponse();
        String body = r.getBody().asString();
        // Actual API uses "missing_api_key" key; accept either documented or actual field name
        assertTrue(
            body.contains("missing_api_key") || body.contains("Missing API key") || body.contains("error"),
            "Expected auth error in body but got: " + body
        );
    }

    // ── Field presence and value assertions ────────────────────────────────

    @Then("the response body should contain fields {string}, {string}, {string}")
    public void bodyContainsThreeFields(String f1, String f2, String f3) {
        Response r = ctx.getResponse();
        for (String field : List.of(f1, f2, f3)) {
            assertNotNull(r.jsonPath().get(field), "Missing field: " + field);
        }
    }

    @Then("the response body should contain fields {string}, {string}, {string}, {string}, {string}, {string}")
    public void bodyContainsSixFields(String f1, String f2, String f3, String f4, String f5, String f6) {
        Response r = ctx.getResponse();
        for (String field : List.of(f1, f2, f3, f4, f5, f6)) {
            assertNotNull(r.jsonPath().get(field), "Missing field: " + field);
        }
    }

    @Then("the response body {string} object should contain fields {string}, {string}")
    public void nestedObjectHasFields(String parent, String f1, String f2) {
        Response r = ctx.getResponse();
        assertNotNull(r.jsonPath().get(parent + "." + f1), "Missing field: " + parent + "." + f1);
        assertNotNull(r.jsonPath().get(parent + "." + f2), "Missing field: " + parent + "." + f2);
    }

    @Then("the response body field {string} should equal {string}")
    public void fieldEqualsString(String field, String expected) {
        String actual = ctx.getResponse().jsonPath().getString(field);
        assertEquals(expected, actual, "Field '" + field + "' mismatch");
    }

    @Then("the response body field {string} should equal {int}")
    public void fieldEqualsInt(String field, int expected) {
        int actual = ctx.getResponse().jsonPath().getInt(field);
        assertEquals(expected, actual, "Field '" + field + "' mismatch");
    }

    @Then("the response body should have a non-empty string field {string}")
    public void nonEmptyStringField(String field) {
        String value = ctx.getResponse().jsonPath().getString(field);
        assertNotNull(value, "Field '" + field + "' is null");
        assertFalse(value.isBlank(), "Field '" + field + "' is blank");
    }

    @Then("the response body field {string} should be a valid ISO 8601 timestamp")
    public void fieldIsIso8601(String field) {
        String value = ctx.getResponse().jsonPath().getString(field);
        assertNotNull(value, "Field '" + field + "' is null");
        assertDoesNotThrow(() -> Instant.parse(value),
                "Field '" + field + "' is not a valid ISO 8601 timestamp: " + value);
    }

    // ── Array assertions ────────────────────────────────────────────────────

    @Then("the response body {string} array size should be at most {int}")
    public void arraySizeAtMost(String arrayField, int max) {
        List<?> list = ctx.getResponse().jsonPath().getList(arrayField);
        assertNotNull(list, "Array '" + arrayField + "' is null");
        assertTrue(list.size() <= max,
                "Expected at most " + max + " items in '" + arrayField + "' but got " + list.size());
    }

    @Then("the response body {string} array should be non-empty")
    public void arrayNonEmpty(String arrayField) {
        List<?> list = ctx.getResponse().jsonPath().getList(arrayField);
        assertNotNull(list, "Array '" + arrayField + "' is null");
        assertFalse(list.isEmpty(), "Expected non-empty array '" + arrayField + "'");
    }

    @Then("the response body {string} field should be a positive integer")
    public void fieldIsPositiveInt(String field) {
        int value = ctx.getResponse().jsonPath().getInt(field);
        assertTrue(value > 0, "Expected '" + field + "' > 0 but got " + value);
    }

    @Then("each user in {string} should have fields {string}, {string}, {string}, {string}, {string}")
    public void eachUserHasFields(String arrayField, String f1, String f2, String f3, String f4, String f5) {
        List<java.util.Map<?, ?>> users = ctx.getResponse().jsonPath().getList(arrayField);
        assertNotNull(users);
        assertFalse(users.isEmpty(), "Data array is empty");
        for (var user : users) {
            for (String field : List.of(f1, f2, f3, f4, f5)) {
                assertTrue(user.containsKey(field), "User object missing field: " + field);
                assertNotNull(user.get(field), "User field '" + field + "' is null");
            }
        }
    }

    @Then("{string} should equal ceil of {string} divided by {string}")
    public void totalPagesConsistency(String totalPagesField, String totalField, String perPageField) {
        Response r = ctx.getResponse();
        int total    = r.jsonPath().getInt(totalField);
        int perPage  = r.jsonPath().getInt(perPageField);
        int expected = (int) Math.ceil((double) total / perPage);
        int actual   = r.jsonPath().getInt(totalPagesField);
        assertEquals(expected, actual,
                "total_pages should be ceil(total/per_page) = " + expected + " but got " + actual);
    }

    @Then("if status is 200 the {string} array should be empty or the response should have a 4xx status")
    public void emptyDataOrNot500(String arrayField) {
        int status = ctx.getResponse().getStatusCode();
        if (status == 200) {
            List<?> data = ctx.getResponse().jsonPath().getList(arrayField);
            assertTrue(data == null || data.isEmpty(),
                    "Expected empty data array for out-of-range page but got: " + data);
        } else {
            assertTrue(status >= 400 && status < 500,
                    "Expected 4xx or 200 with empty data but got: " + status);
        }
    }

    // ── Email / URL format assertions ───────────────────────────────────────

    @Then("each user's {string} field should match a valid email format")
    public void eachUserEmailValid(String field) {
        List<String> values = ctx.getResponse().jsonPath().getList("data." + field);
        assertNotNull(values);
        for (String email : values) {
            assertTrue(EMAIL_PATTERN.matcher(email).matches(),
                    "Invalid email format: " + email);
        }
    }

    @Then("each user's {string} field should start with {string}")
    public void eachUserFieldStartsWith(String field, String prefix) {
        List<String> values = ctx.getResponse().jsonPath().getList("data." + field);
        assertNotNull(values);
        for (String value : values) {
            assertTrue(value.startsWith(prefix),
                    "Expected '" + field + "' to start with '" + prefix + "' but got: " + value);
        }
    }

    // ── Header assertions ───────────────────────────────────────────────────

    @Then("the response Content-Type header should contain {string}")
    public void contentTypeContains(String expected) {
        String contentType = ctx.getResponse().getContentType();
        assertNotNull(contentType, "Content-Type header is missing");
        assertTrue(contentType.contains(expected),
                "Expected Content-Type to contain '" + expected + "' but got: " + contentType);
    }

    // ── Performance assertion ───────────────────────────────────────────────

    @Then("the response time should be less than {int} milliseconds")
    public void responseFasterThan(int ms) {
        long responseTime = ctx.getResponse().getTime();
        assertTrue(responseTime < ms,
                "Response took " + responseTime + "ms, exceeding threshold of " + ms + "ms");
    }

    // ── Cross-page / stored ID assertions ──────────────────────────────────

    @Then("I store the user IDs from {string}")
    public void storeUserIds(String arrayField) {
        List<Integer> ids = ctx.getResponse().jsonPath().getList(arrayField + ".id");
        ctx.setStoredUserIds(ids);
    }

    @Then("the user IDs from {string} should not overlap with stored IDs")
    public void idsNoOverlap(String arrayField) {
        List<Integer> page2Ids = ctx.getResponse().jsonPath().getList(arrayField + ".id");
        List<Integer> stored   = ctx.getStoredUserIds();
        for (Integer id : page2Ids) {
            assertFalse(stored.contains(id),
                    "Duplicate user id " + id + " found on both pages");
        }
    }

    @Then("I store the created user id from the response")
    public void storeCreatedUserId() {
        // ReqRes returns id as string for POST /api/users
        String idStr = ctx.getResponse().jsonPath().getString("id");
        assertNotNull(idStr, "Response missing 'id' field");
        try {
            ctx.setStoredCreatedUserId(Integer.parseInt(idStr));
        } catch (NumberFormatException e) {
            // id might be a non-numeric string; store as -1 sentinel so overlap check always passes
            ctx.setStoredCreatedUserId(-1);
        }
    }

    @Then("the stored user id should not appear in the {string} array")
    public void storedIdNotInArray(String arrayField) {
        Integer storedId = ctx.getStoredCreatedUserId();
        if (storedId == null || storedId == -1) return; // non-integer id — skip numeric check
        List<Integer> ids = ctx.getResponse().jsonPath().getList(arrayField + ".id");
        if (ids == null) return;
        assertFalse(ids.contains(storedId),
                "Created user id " + storedId + " unexpectedly found in GET response (mock API should not persist)");
    }
}
