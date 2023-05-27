package com.vivek.java_testing.controller;

import com.vivek.java_testing.dto.response.ResponseError;
import com.vivek.java_testing.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseError>  customError(CustomException customException){
        final var errorBody =  ResponseError.builder().message(customException.getMessage()).status(customException.getStatus()).errors(customException.getErrors()).build();
        return ResponseEntity.status(errorBody.getStatus()).body(errorBody);
    }
}
