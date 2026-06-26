package com.reqres.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** A single user entry from the {@code data} array of {@code GET /api/users}. */
public record User(
        int id,
        String email,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String avatar) {
}
