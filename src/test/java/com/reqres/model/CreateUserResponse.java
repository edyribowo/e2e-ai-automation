package com.reqres.model;

/** Response body for {@code POST /api/users}. */
public record CreateUserResponse(
        String name,
        String job,
        String id,
        String createdAt) {
}
