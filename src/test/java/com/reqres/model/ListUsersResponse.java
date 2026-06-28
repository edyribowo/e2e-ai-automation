package com.reqres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListUsersResponse {
    public int page;

    @JsonProperty("per_page")
    public int perPage;

    public int total;

    @JsonProperty("total_pages")
    public int totalPages;

    public List<User> data;
    public Support support;
}
