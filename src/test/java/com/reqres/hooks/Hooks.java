package com.reqres.hooks;

import com.reqres.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("▶ Starting: " + scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && context.getResponse() != null) {
            scenario.attach(
                context.getResponse().asByteArray(),
                "application/json",
                "Last Response"
            );
        }
        System.out.println("■ Finished: " + scenario.getName() + " [" + scenario.getStatus() + "]");
    }
}
