package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserListData extends Data<List<User>> {
    protected List<User> users;

    @Override
    public void set(List<User> userList) {
        this.users = userList;
    }
}
