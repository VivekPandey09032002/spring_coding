package com.vivek.java_testing.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseUser {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("user_id")
    private String userId;
}
