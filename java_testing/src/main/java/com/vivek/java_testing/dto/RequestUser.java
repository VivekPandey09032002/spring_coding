package com.vivek.java_testing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestUser {
    @JsonProperty("user_name")
    @NotNull(message = "user name cannot be null")
    private String userName;
    private String description;
    @NotNull(message = "user email cannot be null")
    private String email;
}
