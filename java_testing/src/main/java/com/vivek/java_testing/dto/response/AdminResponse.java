package com.vivek.java_testing.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminResponse {
    @JsonProperty("admin_id")
    private int adminId;
    @JsonProperty("admin_name")
    private String adminName;


}
