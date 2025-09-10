package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;

@Getter
public class Response {
    @JsonView(Views.basicProfileInfo.class)
    protected String status;
    @JsonView(Views.basicProfileInfo.class)
    protected Data<?> data;

    public void set(Data<?> data) {
        this.status = "success";
        this.data = data;
    }
}
