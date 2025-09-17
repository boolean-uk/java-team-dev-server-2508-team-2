package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUser(User user);
    boolean existsByUser(User user);
}
