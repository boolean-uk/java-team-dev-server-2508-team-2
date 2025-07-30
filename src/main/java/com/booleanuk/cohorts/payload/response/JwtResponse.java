package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private User user;


    public JwtResponse(String token, User user) {
        this.token = token;

    }
}
