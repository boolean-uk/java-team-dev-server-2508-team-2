package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserListResponse extends Response {
    public void set(List<User> users) {
        Data<List<User>> data = new UserListData();
        data.set(users);
        super.set(data);
    }


}
