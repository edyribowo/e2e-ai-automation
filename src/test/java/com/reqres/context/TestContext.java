package com.reqres.context;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.List;

public class TestContext {

    private Response response;
    private RequestSpecification requestSpec;
    private String requestBody;
    private Integer storedCreatedUserId;
    private List<Integer> storedUserIds = new ArrayList<>();

    public Response getResponse() { return response; }
    public void setResponse(Response response) { this.response = response; }

    public RequestSpecification getRequestSpec() { return requestSpec; }
    public void setRequestSpec(RequestSpecification requestSpec) { this.requestSpec = requestSpec; }

    public String getRequestBody() { return requestBody; }
    public void setRequestBody(String requestBody) { this.requestBody = requestBody; }

    public Integer getStoredCreatedUserId() { return storedCreatedUserId; }
    public void setStoredCreatedUserId(Integer storedCreatedUserId) { this.storedCreatedUserId = storedCreatedUserId; }

    public List<Integer> getStoredUserIds() { return storedUserIds; }
    public void setStoredUserIds(List<Integer> storedUserIds) { this.storedUserIds = storedUserIds; }
}
