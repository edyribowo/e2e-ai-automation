package com.reqres.client;

import com.reqres.config.ApiConfig;
import com.reqres.context.TestContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private final TestContext context;

    public ApiClient(TestContext context) {
        this.context = context;
    }

    public Response get(String endpoint) {
        return baseRequest().get(endpoint);
    }

    public Response delete(String endpoint) {
        return baseRequest().delete(endpoint);
    }

    public Response post(String endpoint) {
        return withBody(baseRequest()).post(endpoint);
    }

    public Response getWithValidKey(String endpoint, String apiKey) {
        return given()
                .spec(ApiConfig.baseRequestSpec())
                .header(ApiConfig.API_KEY_HEADER, apiKey)
                .get(endpoint);
    }

    private RequestSpecification baseRequest() {
        RequestSpecification req = given().spec(ApiConfig.baseRequestSpec());
        if (context.isApiKeyHeaderPresent()) {
            req.header(ApiConfig.API_KEY_HEADER, context.getApiKey());
        }
        if (context.isContentTypeEnabled()) {
            req.contentType(ContentType.JSON);
        }
        return req;
    }

    private RequestSpecification withBody(RequestSpecification req) {
        Object payload = context.getPayload();
        if (payload != null) {
            req.body(payload);
        }
        return req;
    }
}
