package com.reqres.steps;

import com.reqres.config.ConfigManager;
import com.reqres.context.TestContext;
import io.cucumber.java.en.Given;

/** Steps that control the {@code x-api-key} authentication header. */
public class HeaderSteps {

    private final TestContext context;

    public HeaderSteps(TestContext context) {
        this.context = context;
    }

    @Given("a valid API key is set")
    public void aValidApiKeyIsSet() {
        context.setApiKeyHeaderPresent(true);
        context.setApiKey(ConfigManager.validApiKey());
    }

    @Given("no API key header is set")
    public void noApiKeyHeaderIsSet() {
        context.setApiKeyHeaderPresent(false);
        context.setApiKey(null);
    }

    @Given("the API key is set to {string}")
    public void theApiKeyIsSetTo(String apiKey) {
        // Present but invalid/empty - the header is sent with the given value.
        context.setApiKeyHeaderPresent(true);
        context.setApiKey(apiKey);
    }
}
