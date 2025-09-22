package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        DataResponse<List<User>> userResponse = new DataResponse<>();
        userResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response> getUserById(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        DataResponse<User> userResponse = new DataResponse<>();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public void registerUser() {
        System.out.println("Register endpoint hit");
    }

    private record UserRequest(String email, String password) {}

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable int id, @RequestBody UserRequest request) {
        User userToUpdate = this.userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("User with that id was not found"));
        }
        String email = request.email();
        String password = request.password();
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || email.isBlank() || !email.matches(emailRegex) ||
                password == null || password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[!@#$%^&*()].*")) {

            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid input"));
        }

        if (!userToUpdate.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            return ResponseEntity.status(409).body(new ErrorResponse("Email already in use"));
        }

        userToUpdate.setEmail(email);
        userToUpdate.setPassword(encoder.encode(password));

        userRepository.save(userToUpdate);

        DataResponse<User> userResponse = new DataResponse<>();
        userResponse.set(userToUpdate);
        return ResponseEntity.ok(userResponse);
    }
}
