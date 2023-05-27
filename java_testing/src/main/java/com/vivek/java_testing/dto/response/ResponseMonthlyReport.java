package com.vivek.java_testing.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ResponseMonthlyReport {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("total_attendance")
    private int totalAttendance;
    private int leaves;
    @JsonProperty("no_of_late_days")
    private int lateDays;
    @JsonProperty("total_hour_completion")
    private double hourCompletion;
}
