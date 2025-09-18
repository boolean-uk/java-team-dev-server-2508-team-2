package com.booleanuk.cohorts.payload.response;

import lombok.Getter;

@Getter
public class Data<T> {
    private T data;
    public void set(T data) {
        this.data = data;
    }
}
