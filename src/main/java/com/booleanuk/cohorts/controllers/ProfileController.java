package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.DataResponse;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.MessageResponse;
import com.booleanuk.cohorts.payload.response.Response;
import com.booleanuk.cohorts.repository.*;
import com.booleanuk.cohorts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserService userService;

    @GetMapping("{userId}/profile")
    public ResponseEntity<Response> getUserById(@PathVariable int userId) {
        Profile profile = this.profileRepository.findById(userId).orElse(null);
        if (profile == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        DataResponse<Profile> profileResponse = new DataResponse<>();
        profileResponse.set(profile);
        return ResponseEntity.ok(profileResponse);
    }

    private record profileRequest(String firstName, String lastName, String phone, String githubUrl, String bio, String email, String password, Set<String> roles, int cohortId, int specialisationId, String jobTitle){}

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<?> registerProfile(@PathVariable int userId, @RequestBody profileRequest profileRequest,  Authentication authentication) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        }

        String loggedInEmail = authentication.getName();
        User loggedInUser = userRepository.findByEmail(loggedInEmail).orElse(null);
        if (loggedInUser.getId() != userId && !loggedInUser.isTeacher()) {
            return ResponseEntity.status(401).body(new ErrorResponse("You cannot update another users profile"));
        }

        if (profileRequest.email() != null) {
            ResponseEntity<Response> response = userService.setUserEmail(user, profileRequest.email());
            if (response != null) return response;
        }
        if (loggedInUser.getId() == userId && profileRequest.password() != null) {
            ResponseEntity<Response> response = userService.setUserPassword(user, profileRequest.password());
            if (response != null) return response;
        } else if (profileRequest.password() != null) {
            return ResponseEntity.status(401).body(new ErrorResponse("You cannot update another users password"));
        }

        Profile profile = profileRepository.findByUser(user).orElse(new Profile());
        profile.setUser(user);

        if (profileRequest.firstName() != null) profile.setFirstName(profileRequest.firstName());
        if (profileRequest.lastName() != null) profile.setLastName(profileRequest.lastName());
        if (profileRequest.phone() != null) profile.setPhone(profileRequest.phone());
        if (profileRequest.bio() != null) profile.setBio(profileRequest.bio());
        if (profileRequest.githubUrl() != null) profile.setGithubUrl(profileRequest.githubUrl());

        if (!loggedInUser.isTeacher() && (profileRequest.cohortId() != 0 ||
                profileRequest.specialisationId() != 0 ||
                profileRequest.roles() != null ||
                profileRequest.jobTitle() != null)) {
            return ResponseEntity.status(401).body(new ErrorResponse("You cannot change Training info unless you are a teacher"));
        }

        if (loggedInUser.isTeacher()) {
            if (profileRequest.cohortId() != 0) userService.assignUserToCohort(user, profileRequest.cohortId());
            if (profileRequest.specialisationId() != 0) userService.assignSpecialisation(user, profileRequest.specialisationId());
            if (profileRequest.roles() != null) userService.assignRolesToUser(user, profileRequest.roles);
            if (profileRequest.jobTitle() != null) profile.setJobTitle(profileRequest.jobTitle());
        }

        profileRepository.save(profile);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

}
