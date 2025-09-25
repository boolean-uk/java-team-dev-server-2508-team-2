package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.UserExercise;
import lombok.Getter;

import java.util.List;

@Getter
public class UserExerciseListData extends Data<List<UserExercise>>{
    protected List<UserExercise> userExercises;

    @Override
    public void set(List<UserExercise> list) {
        this.userExercises = list;
    }
}
