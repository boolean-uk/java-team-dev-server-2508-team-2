package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.UserExercise;
import lombok.Getter;

@Getter
public class UserExerciseData extends Data<UserExercise>{
    protected UserExercise userExercise;

    @Override
    public void set(UserExercise userExercise) {
        this.userExercise = userExercise;
    }
}
