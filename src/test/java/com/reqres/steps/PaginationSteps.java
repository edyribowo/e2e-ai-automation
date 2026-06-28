package com.reqres.steps;

import com.reqres.client.ApiClient;
import com.reqres.config.ConfigManager;
import com.reqres.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PaginationSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public PaginationSteps(TestContext context, ApiClient apiClient) {
        this.context = context;
        this.apiClient = apiClient;
    }

    @When("the first two pages of {string} are fetched")
    public void theFirstTwoPagesAreFetched(String endpoint) {
        Response page1 = fetchPage(endpoint, 1);
        Response page2 = fetchPage(endpoint, 2);
        context.put("page1Ids", page1.jsonPath().getList("data.id", Integer.class));
        context.put("page2Ids", page2.jsonPath().getList("data.id", Integer.class));
        context.put("paginationTotal", page1.jsonPath().getInt("total"));
    }

    @Then("there should be no duplicate user ids across the two pages")
    @SuppressWarnings("unchecked")
    public void thereShouldBeNoDuplicateIdsAcrossTheTwoPages() {
        List<Integer> page1 = (List<Integer>) context.get("page1Ids");
        List<Integer> page2 = (List<Integer>) context.get("page2Ids");
        int total           = (int) context.get("paginationTotal");

        Set<Integer> intersection = new HashSet<>(page1);
        intersection.retainAll(page2);
        assertThat("ids shared between page 1 and page 2", intersection, is(empty()));

        List<Integer> combined = new ArrayList<>(page1);
        combined.addAll(page2);
        assertThat("combined ids should be unique", new HashSet<>(combined).size(), is(combined.size()));
        assertThat("combined id count should equal total", combined.size(), is(total));
    }

    @Then("the created user id should not appear in the first two pages of {string}")
    public void theCreatedUserIdShouldNotAppearInTheFirstTwoPages(String endpoint) {
        String createdId = context.getResponse().jsonPath().getString("id");

        List<Integer> page1 = fetchPage(endpoint, 1).jsonPath().getList("data.id", Integer.class);
        List<Integer> page2 = fetchPage(endpoint, 2).jsonPath().getList("data.id", Integer.class);
        List<String> allIds = new ArrayList<>();
        page1.forEach(id -> allIds.add(String.valueOf(id)));
        page2.forEach(id -> allIds.add(String.valueOf(id)));

        assertThat("created id should be absent from the listing",
                allIds, everyItem(is(not(createdId))));
    }

    private Response fetchPage(String endpoint, int page) {
        String url = endpoint + (endpoint.contains("?") ? "&" : "?") + "page=" + page;
        return apiClient.getWithValidKey(url, ConfigManager.validApiKey());
    }
}
