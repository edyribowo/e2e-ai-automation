package com.reqres.steps;

import com.reqres.client.ApiClient;
import com.reqres.context.TestContext;
import io.cucumber.java.en.When;

public class RequestSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public RequestSteps(TestContext context, ApiClient apiClient) {
        this.context = context;
        this.apiClient = apiClient;
    }

    @When("a GET request is sent to {string}")
    public void aGetRequestIsSentTo(String endpoint) {
        context.setResponse(apiClient.get(endpoint));
    }

    @When("a POST request is sent to {string}")
    public void aPostRequestIsSentTo(String endpoint) {
        context.setResponse(apiClient.post(endpoint));
    }

    @When("a DELETE request is sent to {string}")
    public void aDeleteRequestIsSentTo(String endpoint) {
        context.setResponse(apiClient.delete(endpoint));
    }

    @When("a POST request is sent to {string} without a Content-Type header")
    public void aPostRequestIsSentToWithoutContentType(String endpoint) {
        context.setContentTypeEnabled(false);
        context.setResponse(apiClient.post(endpoint));
    }
}
