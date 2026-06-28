package com.reqres.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateUserRequest(String name, String job) {

    public static CreateUserRequest of(String name, String job) {
        return new CreateUserRequest(name, job);
    }

    public static CreateUserRequest nameOnly(String name) {
        return new CreateUserRequest(name, null);
    }

    public static CreateUserRequest jobOnly(String job) {
        return new CreateUserRequest(null, job);
    }
}
