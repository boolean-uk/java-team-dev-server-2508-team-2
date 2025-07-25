package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.ERole;
import com.booleanuk.cohorts.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}
