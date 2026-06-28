package com.reqres.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

import static io.restassured.config.EncoderConfig.encoderConfig;

public final class ApiConfig {

    public static final String API_KEY_HEADER = "x-api-key";

    private ApiConfig() {}

    // Content-Type is NOT set globally so individual scenarios can omit it (e.g. TC-X-004).
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
