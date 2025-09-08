package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.ERole;
import com.booleanuk.cohorts.models.Role;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.request.LoginRequest;
import com.booleanuk.cohorts.payload.request.SignupRequest;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.JwtResponse;
import com.booleanuk.cohorts.payload.response.MessageResponse;
import com.booleanuk.cohorts.payload.response.TokenResponse;
import com.booleanuk.cohorts.repository.RoleRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import com.booleanuk.cohorts.security.jwt.JwtUtils;
import com.booleanuk.cohorts.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // If using a salt for password use it here
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map((item) -> item.getAuthority())
                .collect(Collectors.toList());

        User user = userRepository.findByEmail(userDetails.getEmail()).orElse(null);

        return ResponseEntity
                .ok(new TokenResponse(new JwtResponse(jwt, user)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || email.isBlank() || !email.matches(emailRegex) ||
                password == null || password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[!@#$%^&*()].*")) {

            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid input"));
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body(new ErrorResponse("Email already in use"));
        }

        User user = new User(email, encoder.encode(password));
        if (signupRequest.getCohort() != null) {
            user.setCohort(signupRequest.getCohort());
        }

        Set<String> strRoles = signupRequest.getRole();
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

        userRepository.save(user);

        return ResponseEntity.status(201).body(new MessageResponse("User registered successfully"));
    }

}