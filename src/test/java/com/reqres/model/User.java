package com.reqres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
        int id,
        String email,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String avatar
) {}
