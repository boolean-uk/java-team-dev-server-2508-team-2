package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Exercise;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.models.UserExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserExerciseRepository extends JpaRepository<UserExercise, Integer> {
    List<UserExercise> findAllByUser(User user);
    Optional<UserExercise> findByUserAndExercise(User user, Exercise exercise);
}
