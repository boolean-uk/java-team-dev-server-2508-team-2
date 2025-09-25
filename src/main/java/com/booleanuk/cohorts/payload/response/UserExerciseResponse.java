package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.UserExercise;

public class UserExerciseResponse extends Response{
    public void set(UserExercise userExercise) {
        Data<UserExercise> data = new UserExerciseData();
        data.set(userExercise);
        super.set(data);
    }
}
