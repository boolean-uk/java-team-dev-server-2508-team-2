package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
    Optional<Unit> findByName(String name);
    Boolean existsByName(String name);
}
