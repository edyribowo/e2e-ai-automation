package com.reqres.steps;

import com.reqres.context.TestContext;
import com.reqres.model.CreateUserRequest;
import io.cucumber.java.en.Given;

import java.util.LinkedHashMap;
import java.util.Map;

/** Steps that prepare the request body for {@code POST /api/users}. */
public class PayloadSteps {

    private final TestContext context;

    public PayloadSteps(TestContext context) {
        this.context = context;
    }

    @Given("a user payload with name {string} and job {string}")
    public void aUserPayloadWithNameAndJob(String name, String job) {
        context.setRawBody(false);
        context.setPayload(CreateUserRequest.of(name, job));
    }

    @Given("a user payload with only name {string}")
    public void aUserPayloadWithOnlyName(String name) {
        context.setRawBody(false);
        context.setPayload(CreateUserRequest.nameOnly(name));
    }

    @Given("a user payload with only job {string}")
    public void aUserPayloadWithOnlyJob(String job) {
        context.setRawBody(false);
        context.setPayload(CreateUserRequest.jobOnly(job));
    }

    @Given("an empty JSON payload")
    public void anEmptyJsonPayload() {
        context.setRawBody(false);
        context.setPayload(new LinkedHashMap<String, Object>());
    }

    @Given("a user payload with the following fields:")
    public void aUserPayloadWithTheFollowingFields(Map<String, String> fields) {
        context.setRawBody(false);
        context.setPayload(new LinkedHashMap<String, Object>(fields));
    }

    @Given("a user payload with a {int}-character name and a {int}-character job")
    public void aUserPayloadWithLongStrings(int nameLength, int jobLength) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("name", "a".repeat(nameLength));
        body.put("job", "b".repeat(jobLength));
        context.setRawBody(false);
        context.setPayload(body);
    }

    @Given("a user payload with numeric name {int} and numeric job {int}")
    public void aUserPayloadWithNumericValues(int name, int job) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("name", name);
        body.put("job", job);
        context.setRawBody(false);
        context.setPayload(body);
    }

    @Given("a raw request body {string}")
    public void aRawRequestBody(String rawBody) {
        context.setRawBody(true);
        context.setPayload(rawBody);
    }
}
