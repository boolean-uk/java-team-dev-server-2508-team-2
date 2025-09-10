package com.booleanuk.cohorts.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse extends Response {
    public ErrorResponse(String message) {
        Data<String> data = new ErrorData();
        data.set(message);
        super.set(data);
        this.status = "error";

    }

    public void set(String message) {
        Data<String> data = new ErrorData();
        data.set(message);
        super.set(data);
        this.status = "error";

    }
}
