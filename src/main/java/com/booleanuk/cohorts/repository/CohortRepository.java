package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CohortRepository extends JpaRepository<Cohort, Integer> {
}
