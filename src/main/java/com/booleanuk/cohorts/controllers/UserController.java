package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Cohort;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.models.UserExercise;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.UserExerciseRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.Response;
import com.booleanuk.cohorts.payload.response.UserListResponse;
import com.booleanuk.cohorts.payload.response.UserResponse;
import com.booleanuk.cohorts.validation.UserValidator;
import com.booleanuk.cohorts.validation.ValidationError;
import jakarta.validation.constraints.Null;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.CohortRepository;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.UserRepository;
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

    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private CohortRepository cohortRepository;

    @GetMapping
    public ResponseEntity<Response> getAllUsers() {
        DataResponse<List<User>> userListResponse = new DataResponse<>();
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
        DataResponse<User> userResponse = new DataResponse<>();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("{id}/exercises")
    public ResponseEntity<Response> getExercises(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);

        ValidationError validationError = userValidator.validateExistingStudent(user);
        if (validationError != null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set(validationError.getMessage());
            return new ResponseEntity<>(errorResponse, validationError.getStatus());
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

    @PutMapping("{id}/cohort/{cohortId}")
    public ResponseEntity<Response> assignCohortToStudent(@PathVariable int id, @PathVariable int cohortId){
        User user = this.userRepository.findById(id).orElse(null);

        if(user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Student not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if(!user.isStudent()){
            ErrorResponse error = new ErrorResponse();
            error.set("Only student can be assigned to a cohort");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Cohort cohort = this.cohortRepository.findById(cohortId).orElse(null);

        if(cohort == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Cohort not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        user.setCohort(cohort);
        userRepository.save(user);

        DataResponse<User> userResponse = new DataResponse<>();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public void registerUser() {
        System.out.println("Register endpoint hit");
    }
}
