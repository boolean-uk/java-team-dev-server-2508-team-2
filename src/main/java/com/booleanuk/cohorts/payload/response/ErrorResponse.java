package com.booleanuk.cohorts.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse extends Response {
    public ErrorResponse(String message) {
        this.status = "error";
        Data<String> data = new ErrorData();
        data.set(message);
        super.set(data);
    }

    public void set(String message) {
        this.status = "error";
        Data<String> data = new ErrorData();
        data.set(message);
        super.set(data);
    }
}
