package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Cohort;
import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.CohortRepository;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.ProfileRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import com.booleanuk.cohorts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserService userService;

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

    private record userRequest(String firstName, String lastName, String phone, String githubUrl, String bio, String email, String password, Set<String> roles, int cohortId, int specialisationId, String jobTitle){}

    @PostMapping
    public ResponseEntity<Response> registerUser(@RequestBody userRequest userRequest) {
        User newUser = new User();
        ResponseEntity<Response> emailResponse = userService.setUserEmail(newUser, userRequest.email());
        if (emailResponse != null) return emailResponse;

        ResponseEntity<Response> passwordResponse = userService.setUserPassword(newUser, userRequest.password());
        if (passwordResponse != null) return passwordResponse;

        if (userRequest.cohortId() != 0) userService.assignUserToCohort(newUser, userRequest.cohortId());
        if (userRequest.specialisationId() !=0) userService.assignSpecialisation(newUser, userRequest.specialisationId());

        userService.assignRolesToUser(newUser, userRequest.roles());

        Profile profile = new Profile(newUser,
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.phone(),
                userRequest.bio(),
                userRequest.githubUrl);

        if (userRequest.jobTitle != null) profile.setJobTitle(userRequest.jobTitle());

        userRepository.save(newUser);
        profileRepository.save(profile);

        DataResponse<User> userResponse = new DataResponse<>();
        userResponse.set(newUser);
        return ResponseEntity.ok(userResponse);
    }
}
