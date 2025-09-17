package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Specialisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface SpecialisationRepository extends JpaRepository<Specialisation, Integer> {
}
