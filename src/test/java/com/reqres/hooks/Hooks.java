package com.reqres.hooks;

import com.reqres.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;

/**
 * Lifecycle hooks. Enables Rest Assured's URL-encoding safety net before every
 * scenario and attaches the final response body to the Allure report on failure.
 */
public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario() {
        RestAssured.urlEncodingEnabled = true;
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed() && context.getResponse() != null) {
            String body = context.getResponse().getBody().asPrettyString();
            scenario.attach(body, "application/json", "Final response body");
        }
    }
}
