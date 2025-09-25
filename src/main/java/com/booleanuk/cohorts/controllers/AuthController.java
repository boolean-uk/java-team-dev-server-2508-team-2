package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.request.LoginRequest;
import com.booleanuk.cohorts.payload.request.SignupRequest;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.UserRepository;
import com.booleanuk.cohorts.security.jwt.JwtUtils;
import com.booleanuk.cohorts.security.services.UserDetailsImpl;
import com.booleanuk.cohorts.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;
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
    UserService userService;

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
        User user = new User();

        ResponseEntity<Response> emailResponse = userService.setUserEmail(user, signupRequest.getEmail());
        if (emailResponse != null) return emailResponse;

        ResponseEntity<Response> response = userService.setUserPassword(user, signupRequest.getPassword());
        if (response != null) return response;

        if (signupRequest.getCohort() != null) {
            user.setCohort(signupRequest.getCohort());
        }

        userService.assignRolesToUser(user, signupRequest.getRole());

        userRepository.save(user);

        return ResponseEntity.status(201).body(new MessageResponse("User registered successfully"));
    }

}