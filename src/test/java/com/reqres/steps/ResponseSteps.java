package com.reqres.steps;

import com.reqres.context.TestContext;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.oneOf;

/** Generic, endpoint-agnostic assertions on the captured response. */
public class ResponseSteps {

    private final TestContext context;

    public ResponseSteps(TestContext context) {
        this.context = context;
    }

    private Response response() {
        return context.getResponse();
    }

    private JsonPath json() {
        return response().jsonPath();
    }

    // ---- Status code -------------------------------------------------------

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expected) {
        assertThat("status code", response().statusCode(), is(expected));
    }

    @Then("the response status code should be one of {string}")
    public void theResponseStatusCodeShouldBeOneOf(String csv) {
        List<Integer> allowed = Arrays.stream(csv.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
        assertThat("status code", response().statusCode(), is(oneOf(allowed.toArray(new Integer[0]))));
    }

    @Then("the response should not be a server error")
    public void theResponseShouldNotBeAServerError() {
        assertThat("status code should be below 500", response().statusCode(), is(lessThan(500)));
    }

    // ---- Field value assertions -------------------------------------------

    @Then("the response field {string} should equal {string}")
    public void theResponseFieldShouldEqualString(String field, String expected) {
        Object actual = json().get(field);
        assertThat("field '" + field + "'", String.valueOf(actual), is(equalTo(expected)));
    }

    @Then("the response field {string} should equal {int}")
    public void theResponseFieldShouldEqualInt(String field, int expected) {
        assertThat("field '" + field + "'", json().getInt(field), is(expected));
    }

    @Then("the response field {string} should be a non-empty string")
    public void theResponseFieldShouldBeANonEmptyString(String field) {
        Object value = json().get(field);
        assertThat("field '" + field + "'", value, is(notNullValue()));
        assertThat("field '" + field + "'", value, is(instanceOf(String.class)));
        assertThat("field '" + field + "' should not be blank", ((String) value).isBlank(), is(false));
    }

    @Then("the response field {string} should have length {int}")
    public void theResponseFieldShouldHaveLength(String field, int expectedLength) {
        String value = json().getString(field);
        assertThat("field '" + field + "'", value, is(notNullValue()));
        assertThat("length of '" + field + "'", value.length(), is(expectedLength));
    }

    @Then("the response field {string} should be a positive integer")
    public void theResponseFieldShouldBeAPositiveInteger(String field) {
        assertThat("field '" + field + "'", json().getInt(field), is(greaterThan(0)));
    }

    @Then("the response field {string} should be an array")
    public void theResponseFieldShouldBeAnArray(String field) {
        assertThat("field '" + field + "'", json().get(field), is(instanceOf(List.class)));
    }

    // ---- Timestamps --------------------------------------------------------

    @Then("the response field {string} should be a valid ISO 8601 timestamp")
    public void theResponseFieldShouldBeAValidIso8601Timestamp(String field) {
        String value = json().getString(field);
        assertThat("field '" + field + "'", value, is(notNullValue()));
        // Throws if the value is not a valid ISO-8601 instant.
        OffsetDateTime.parse(value);
    }

    @Then("the {string} timestamp should be within {int} seconds of now")
    public void theTimestampShouldBeWithinSecondsOfNow(String field, int seconds) {
        Instant timestamp = OffsetDateTime.parse(json().getString(field)).toInstant();
        long deltaSeconds = Math.abs(Duration.between(timestamp, Instant.now()).getSeconds());
        assertThat("'" + field + "' should be recent", deltaSeconds, is(lessThan((long) seconds)));
    }

    // ---- Body shape --------------------------------------------------------

    @Then("the response body should contain the field {string}")
    public void theResponseBodyShouldContainTheField(String field) {
        assertThat("field '" + field + "'", json().get(field), is(notNullValue()));
    }

    @Then("the response body should contain the fields {string}")
    public void theResponseBodyShouldContainTheFields(String csv) {
        for (String field : csv.split(",")) {
            String name = field.trim();
            assertThat("field '" + name + "'", json().get(name), is(notNullValue()));
        }
    }

    @Then("the response should contain an error message")
    public void theResponseShouldContainAnErrorMessage() {
        String error = json().getString("error");
        assertThat("an error message", error, is(notNullValue()));
        assertThat("error message should not be blank", error.isBlank(), is(false));
    }

    // ---- Headers & timing --------------------------------------------------

    @Then("the response Content-Type should be JSON")
    public void theResponseContentTypeShouldBeJson() {
        assertThat("Content-Type", response().getContentType(), containsString("application/json"));
    }

    @Then("the response time should be under {int} ms")
    public void theResponseTimeShouldBeUnderMs(int maxMillis) {
        assertThat("response time (ms)", response().getTime(), is(lessThan((long) maxMillis)));
    }
}
