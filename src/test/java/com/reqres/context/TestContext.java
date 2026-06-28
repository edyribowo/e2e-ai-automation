package com.reqres.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-scenario shared state. PicoContainer creates a new instance for every
 * scenario and injects it into each step-definition class — no static fields,
 * so the suite is safe for parallel execution.
 */
public class TestContext {

    private boolean apiKeyHeaderPresent = false;
    private String apiKey;
    private Object payload;
    private boolean contentTypeEnabled = true;
    private Response response;
    private final Map<String, Object> store = new HashMap<>();

    public boolean isApiKeyHeaderPresent() { return apiKeyHeaderPresent; }
    public void setApiKeyHeaderPresent(boolean v) { this.apiKeyHeaderPresent = v; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }

    public boolean isContentTypeEnabled() { return contentTypeEnabled; }
    public void setContentTypeEnabled(boolean v) { this.contentTypeEnabled = v; }

    public Response getResponse() { return response; }
    public void setResponse(Response response) { this.response = response; }

    public void put(String key, Object value) { store.put(key, value); }
    public Object get(String key) { return store.get(key); }
}
