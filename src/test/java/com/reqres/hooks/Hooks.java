package com.reqres.hooks;

import com.reqres.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

/** Attaches the response body to the Allure report when a scenario fails. */
public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed() && context.getResponse() != null) {
            String body = context.getResponse().getBody().asPrettyString();
            scenario.attach(body, "application/json", "Final response body");
        }
    }
}
