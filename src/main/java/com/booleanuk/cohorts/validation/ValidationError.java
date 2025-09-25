package com.booleanuk.cohorts.validation;

import org.springframework.http.HttpStatus;

public class ValidationError {
    private HttpStatus status;
    private String message;

    public ValidationError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
