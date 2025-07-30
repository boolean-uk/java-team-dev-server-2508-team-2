package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String status;
    private JwtResponse data;

    public TokenResponse(JwtResponse token) {
        this.status = "success";
        this.data = token;

    }
}
