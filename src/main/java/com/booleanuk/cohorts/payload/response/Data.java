package com.booleanuk.cohorts.payload.response;

import lombok.Getter;

@Getter
public abstract class Data<T> {
    public abstract void set(T data);
}
