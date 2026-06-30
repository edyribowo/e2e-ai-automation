package com.reqres.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiConfig {

    public static final String BASE_URI = "https://reqres.in";
    public static final String API_KEY_HEADER = "x-api-key";
    public static final int LATENCY_THRESHOLD_MS = 3000;

    public static String getApiKey() {
        String key = System.getenv("REQRES_API_KEY");
        if (key == null || key.isBlank()) {
            key = System.getProperty("REQRES_API_KEY");
        }
        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                "REQRES_API_KEY is not configured. " +
                "Obtain a valid key from https://app.reqres.in/api-keys and set it as " +
                "the REQRES_API_KEY environment variable or -DREQRES_API_KEY=<key> system property."
            );
        }
        return key;
    }

    public static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader(API_KEY_HEADER, getApiKey())
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification noAuthSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification invalidKeySpec(String key) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader(API_KEY_HEADER, key)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification noContentTypeSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addHeader(API_KEY_HEADER, getApiKey())
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }
}
