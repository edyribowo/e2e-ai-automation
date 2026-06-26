package com.reqres.steps;

import com.reqres.context.TestContext;
import com.reqres.model.User;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

/** Schema and per-item validation for {@code GET /api/users} list responses. */
public class ListSchemaSteps {

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final TestContext context;

    public ListSchemaSteps(TestContext context) {
        this.context = context;
    }

    private JsonPath json() {
        return context.getResponse().jsonPath();
    }

    @Then("the {string} array should contain at most {int} items")
    public void theArrayShouldContainAtMostItems(String field, int max) {
        assertThat("size of '" + field + "'", json().getList(field).size(), is(lessThanOrEqualTo(max)));
    }

    @Then("the {string} array should not be empty")
    public void theArrayShouldNotBeEmpty(String field) {
        assertThat("size of '" + field + "'", json().getList(field).size(), is(greaterThan(0)));
    }

    @Then("the response should have the integer fields {string}")
    public void theResponseShouldHaveIntegerFields(String csv) {
        for (String field : csv.split(",")) {
            String name = field.trim();
            assertThat("integer field '" + name + "'", json().get(name), is(notNullValue()));
            assertThat("field '" + name + "' should be an integer",
                    json().get(name), is(org.hamcrest.Matchers.instanceOf(Integer.class)));
        }
    }

    @Then("the response should have a {string} object with fields {string}")
    public void theResponseShouldHaveAnObjectWithFields(String objectName, String csv) {
        Map<String, Object> object = json().getMap(objectName);
        assertThat("'" + objectName + "' object", object, is(notNullValue()));
        for (String field : csv.split(",")) {
            String name = field.trim();
            assertThat("'" + objectName + "." + name + "'", object.get(name), is(notNullValue()));
        }
    }

    @Then("each user in {string} should have an integer {string}")
    public void eachUserShouldHaveAnIntegerField(String arrayField, String userField) {
        for (Map<String, Object> user : users(arrayField)) {
            assertThat("'" + userField + "'", user.get(userField), is(org.hamcrest.Matchers.instanceOf(Integer.class)));
        }
    }

    @Then("each user in {string} should have a valid email {string}")
    public void eachUserShouldHaveAValidEmail(String arrayField, String userField) {
        for (Map<String, Object> user : users(arrayField)) {
            String email = String.valueOf(user.get(userField));
            assertThat("email '" + email + "' should match the email format",
                    EMAIL.matcher(email).matches(), is(true));
        }
    }

    @Then("each user in {string} should have non-empty {string} and {string}")
    public void eachUserShouldHaveNonEmptyFields(String arrayField, String f1, String f2) {
        for (Map<String, Object> user : users(arrayField)) {
            assertNonBlank(user, f1);
            assertNonBlank(user, f2);
        }
    }

    @Then("each user in {string} should have a valid URL {string}")
    public void eachUserShouldHaveAValidUrl(String arrayField, String userField) {
        for (Map<String, Object> user : users(arrayField)) {
            String value = String.valueOf(user.get(userField));
            URI uri = URI.create(value); // throws on a malformed URL
            assertThat("'" + value + "' should have an http(s) scheme",
                    uri.getScheme() != null && uri.getScheme().startsWith("http"), is(true));
        }
    }

    @Then("each user in {string} should have an {string} URL starting with {string}")
    public void eachUserShouldHaveAUrlStartingWith(String arrayField, String userField, String prefix) {
        for (Map<String, Object> user : users(arrayField)) {
            String value = String.valueOf(user.get(userField));
            assertThat("'" + value + "' should start with '" + prefix + "'",
                    value.startsWith(prefix), is(true));
        }
    }

    @Then("the total_pages should equal the ceiling of total divided by per_page")
    public void totalPagesShouldEqualCeilOfTotalOverPerPage() {
        int total = json().getInt("total");
        int perPage = json().getInt("per_page");
        int totalPages = json().getInt("total_pages");
        int expected = (int) Math.ceil((double) total / perPage);
        assertThat("total_pages", totalPages, is(expected));
    }

    /**
     * Demonstrates POJO mapping: deserializes the data array into records so the
     * full user schema is validated by type (TC-LST-005).
     */
    @Then("each user in {string} maps to the User schema")
    public void eachUserMapsToTheUserSchema(String arrayField) {
        List<User> mapped = json().getList(arrayField, User.class);
        for (User user : mapped) {
            assertThat("id", user.id(), is(greaterThan(0)));
            assertThat("email", user.email(), is(notNullValue()));
            assertThat("avatar", user.avatar(), is(notNullValue()));
        }
    }

    private List<Map<String, Object>> users(String arrayField) {
        return json().getList(arrayField);
    }

    private void assertNonBlank(Map<String, Object> user, String field) {
        Object value = user.get(field);
        assertThat("'" + field + "'", value, is(notNullValue()));
        assertThat("'" + field + "' should not be blank", String.valueOf(value).isBlank(), is(false));
    }
}
