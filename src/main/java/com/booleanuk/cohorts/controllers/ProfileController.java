package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.MessageResponse;
import com.booleanuk.cohorts.repository.ProfileRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<?> registerProfile(@PathVariable int userId, @RequestBody Profile profileRequest) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        }

        Profile profile = profileRepository.findByUser(user).orElse(new Profile());
        profile.setUser(user);

        if (profileRequest.getFirstName() != null) profile.setFirstName(profileRequest.getFirstName());
        if (profileRequest.getLastName() != null) profile.setLastName(profileRequest.getLastName());
        if (profileRequest.getUsername() != null) profile.setUsername(profileRequest.getUsername());
        if (profileRequest.getPhone() != null) profile.setPhone(profileRequest.getPhone());
        if (profileRequest.getBio() != null) profile.setBio(profileRequest.getBio());
        if (profileRequest.getGithubUrl() != null) profile.setGithubUrl(profileRequest.getGithubUrl());

        profileRepository.save(profile);

        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

}
