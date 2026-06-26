package com.reqres.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

import static io.restassured.config.EncoderConfig.encoderConfig;

/**
 * Centralizes Rest Assured request configuration so headers, base URI, logging
 * and the Allure filter are not duplicated across step definitions.
 *
 * <p>Content-Type is intentionally NOT set here so individual scenarios can omit
 * it (see TC-X-004). The {@code x-api-key} header is applied per-request by
 * {@link com.reqres.client.ApiClient} based on the scenario's state.
 */
public final class ApiConfig {

    public static final String API_KEY_HEADER = "x-api-key";

    private ApiConfig() {
    }

    public static RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigManager.baseUri())
                .addFilter(new AllureRestAssured())
                .setConfig(RestAssuredConfig.config()
                        .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
                .log(LogDetail.ALL)
                .build();
    }
}
