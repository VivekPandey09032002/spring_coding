package com.vivek.java_testing.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSingle<T> {
    private T data;
    private HttpStatus status;
    private String message;
}
