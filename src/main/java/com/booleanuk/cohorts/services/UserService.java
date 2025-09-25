package com.booleanuk.cohorts.services;

import com.booleanuk.cohorts.models.ERole;
import com.booleanuk.cohorts.models.Role;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.Response;
import com.booleanuk.cohorts.repository.CohortRepository;
import com.booleanuk.cohorts.repository.RoleRepository;
import com.booleanuk.cohorts.repository.SpecialisationRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private SpecialisationRepository specialisationRepository;

    @Autowired
    PasswordEncoder encoder;

    public void assignRolesToUser(User user, Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found"));
            roles.add(studentRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                        break;
                    case "teacher":
                        Role teacherRole = roleRepository.findByName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(teacherRole);
                        break;
                    default:
                        Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(studentRole);
                }
            });
        }
        user.setRoles(roles);
    }

    public void assignUserToCohort(User user, int cohortId) {
        user.setCohort(cohortRepository.findById(cohortId).orElseThrow(
                () -> new RuntimeException("Error: Cohort not found")));
    }

    public void assignSpecialisation(User user, int specialisation_id) {
        user.setSpecialisation(specialisationRepository.findById(specialisation_id).orElseThrow(
                () -> new RuntimeException("Error: Specialisation not found")));
    }

    public ResponseEntity<Response> setUserPassword(User user, String password) {
        if (password == null ||
                password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[!@#$%^&*()].*")) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid password"));
        }
        user.setPassword(encoder.encode(password));
        return null;
    }

    public ResponseEntity<Response> setUserEmail(User user, String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || email.isBlank() || !email.matches(emailRegex)) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid email"));
        }
        if (!Objects.equals(user.getEmail(), email) && userRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body(new ErrorResponse("Email is already in use"));
        }
        user.setEmail(email);
        return null;
    }
}
