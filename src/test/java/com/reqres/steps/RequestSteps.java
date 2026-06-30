package com.reqres.steps;

import com.reqres.config.ApiConfig;
import com.reqres.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class RequestSteps {

    private final TestContext ctx;

    public RequestSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("the request uses a valid API key")
    public void useValidApiKey() {
        ctx.setRequestSpec(ApiConfig.baseSpec());
    }

    @Given("the request has no API key header")
    public void useNoApiKey() {
        ctx.setRequestSpec(ApiConfig.noAuthSpec());
    }

    @Given("the request uses an invalid API key {string}")
    public void useInvalidApiKey(String key) {
        ctx.setRequestSpec(ApiConfig.invalidKeySpec(key));
    }

    @Given("the request content type is {string}")
    public void setContentType(String contentType) {
        // Content-Type is already set in the spec; this step is declarative for readability.
        // If spec is not yet set, default to base spec.
        if (ctx.getRequestSpec() == null) {
            ctx.setRequestSpec(ApiConfig.baseSpec());
        }
    }

    @Given("the request has no Content-Type header")
    public void noContentTypeHeader() {
        ctx.setRequestSpec(ApiConfig.noContentTypeSpec());
    }

    @When("a GET request is sent to {string}")
    public void sendGet(String path) {
        ctx.setResponse(
            given().spec(ctx.getRequestSpec()).when().get(path).then().extract().response()
        );
    }

    @When("a POST request is sent to {string} with body:")
    public void sendPost(String path, String body) {
        ctx.setResponse(
            given().spec(ctx.getRequestSpec()).body(body).when().post(path).then().extract().response()
        );
    }

    @When("a POST request is sent to {string} with raw body {string}")
    public void sendPostRawBody(String path, String body) {
        ctx.setResponse(
            given().spec(ctx.getRequestSpec()).body(body).when().post(path).then().extract().response()
        );
    }

    @When("a POST request is sent to {string} with a 1000-character name and job")
    public void sendPostLongStrings(String path) {
        String longName = "a".repeat(1000);
        String longJob  = "b".repeat(1000);
        String body = "{\"name\":\"" + longName + "\",\"job\":\"" + longJob + "\"}";
        ctx.setResponse(
            given().spec(ctx.getRequestSpec()).body(body).when().post(path).then().extract().response()
        );
    }

    @When("a DELETE request is sent to {string}")
    public void sendDelete(String path) {
        ctx.setResponse(
            given().spec(ctx.getRequestSpec()).when().delete(path).then().extract().response()
        );
    }
}
