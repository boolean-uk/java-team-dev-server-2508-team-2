package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.models.UserExercise;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.UserExerciseRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserExerciseRepository userExerciseRepository;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers() {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response> getUserById(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("{id}/exercises")
    public ResponseEntity<Response> getExercises(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if (!user.isStudent()) {
            ErrorResponse error = new ErrorResponse();
            error.set("Exercises are only available for students");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        List<UserExercise> userExercises = userExerciseRepository.findAllByUser(user);

        if (userExercises.isEmpty()) {
            ErrorResponse error = new ErrorResponse();
            error.set("No exercises found for this student");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        UserExerciseListResponse listResponse = new UserExerciseListResponse();
        listResponse.set(userExercises);

        return ResponseEntity.ok(listResponse);
    }

    @PostMapping
    public void registerUser() {
        System.out.println("Register endpoint hit");
    }
}
