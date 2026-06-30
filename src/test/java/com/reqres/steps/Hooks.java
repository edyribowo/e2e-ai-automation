package com.reqres.steps;

import com.reqres.config.ApiConfig;
import com.reqres.context.TestContext;
import io.cucumber.java.Before;

public class Hooks {

    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 0)
    public void resetContext() {
        ctx.setResponse(null);
        ctx.setRequestSpec(null);
        ctx.setRequestBody(null);
        ctx.setStoredCreatedUserId(null);
        ctx.getStoredUserIds().clear();
    }

    @Before(value = "not @negative", order = 10)
    public void setupAuthenticatedSpec() {
        ctx.setRequestSpec(ApiConfig.baseSpec());
    }
}
