package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.User;
import lombok.Getter;

@Getter
public class UserData extends Data<User> {
    protected User user;

    @Override
    public void set(User user) {
        this.user = user;
    }
}
