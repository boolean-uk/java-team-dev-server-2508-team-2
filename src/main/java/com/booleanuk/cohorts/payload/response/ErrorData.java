package com.booleanuk.cohorts.payload.response;

import lombok.Getter;

@Getter
public class ErrorData extends Data<String> {
    protected String message;

    @Override
    public void set(String message) {
        this.message = message;
    }
}
