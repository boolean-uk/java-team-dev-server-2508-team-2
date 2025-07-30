package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.User;
import lombok.Getter;

@Getter
public class UserResponse extends Response{

    public void set(User user) {
        Data<User> data = new UserData();
        data.set(user);
        super.set(data);
    }
}
