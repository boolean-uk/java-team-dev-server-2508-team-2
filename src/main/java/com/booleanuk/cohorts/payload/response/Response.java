package com.booleanuk.cohorts.payload.response;

import lombok.Getter;

@Getter
public class Response {
    protected String status;
    protected Data<?> data;

    public void set(Data<?> data) {
        this.data = data;
    }
}
