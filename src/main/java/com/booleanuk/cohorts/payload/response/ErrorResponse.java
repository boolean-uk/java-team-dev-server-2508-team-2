package com.booleanuk.cohorts.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class ErrorResponse extends Response{
    public void set(String message) {
        this.status = "error";
        Data<String> data = new ErrorData();
        data.set(message);
        super.set(data);
    }
}
