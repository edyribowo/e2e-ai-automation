package com.reqres.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Per-scenario shared state. A new instance is created for every scenario by
 * PicoContainer and injected into each step-definition class, so no {@code static}
 * fields are used (which keeps the suite safe for parallel execution).
 */
public class TestContext {

    /** Whether the {@code x-api-key} header should be sent at all. */
    private boolean apiKeyHeaderPresent = false;

    /** The value to send for {@code x-api-key} when the header is present. */
    private String apiKey;

    /** Request body: a record/Map (serialized as JSON) or a raw String. */
    private Object payload;

    /** When false, the request is sent without a Content-Type header. */
    private boolean contentTypeEnabled = true;

    private Response response;

    /** Scratch space for data carried between steps (created ids, page snapshots, ...). */
    private final Map<String, Object> store = new HashMap<>();

    public boolean isApiKeyHeaderPresent() {
        return apiKeyHeaderPresent;
    }

    public void setApiKeyHeaderPresent(boolean apiKeyHeaderPresent) {
        this.apiKeyHeaderPresent = apiKeyHeaderPresent;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public boolean isContentTypeEnabled() {
        return contentTypeEnabled;
    }

    public void setContentTypeEnabled(boolean contentTypeEnabled) {
        this.contentTypeEnabled = contentTypeEnabled;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }
}
