package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
