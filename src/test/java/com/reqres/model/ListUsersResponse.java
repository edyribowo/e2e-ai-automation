package com.reqres.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** Full response body for {@code GET /api/users}. */
public record ListUsersResponse(
        int page,
        @JsonProperty("per_page") int perPage,
        int total,
        @JsonProperty("total_pages") int totalPages,
        List<User> data,
        Support support) {
}
