package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Cohort;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.Response;
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
    private CohortRepository cohortRepository;

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
