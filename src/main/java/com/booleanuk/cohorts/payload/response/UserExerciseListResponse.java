package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.UserExercise;

import java.util.List;

public class UserExerciseListResponse extends Response{
    public void set(List<UserExercise> userExercises) {
        Data<List<UserExercise>> data = new UserExerciseListData();
        data.set(userExercises);
        super.set(data);
    }
}
