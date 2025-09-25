package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    boolean existsByName(String name);
    Optional<Exercise> findByName(String name);
}
