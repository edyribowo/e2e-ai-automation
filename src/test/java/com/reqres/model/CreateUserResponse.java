package com.reqres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponse {
    public String name;
    public String job;
    public String id;
    public String createdAt;
}
