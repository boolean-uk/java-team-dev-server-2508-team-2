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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private record profileRequest(String firstName, String lastName, String phone, String githubUrl, String bio){}

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<?> registerProfile(@PathVariable int userId, @RequestBody profileRequest profileRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        }

        Profile profile = profileRepository.findByUser(user).orElse(new Profile());
        profile.setUser(user);

        if (profileRequest.firstName() != null) profile.setFirstName(profileRequest.firstName());
        if (profileRequest.lastName() != null) profile.setLastName(profileRequest.lastName());
        if (profileRequest.phone() != null) profile.setPhone(profileRequest.phone());
        if (profileRequest.bio() != null) profile.setBio(profileRequest.bio());
        if (profileRequest.githubUrl() != null) profile.setGithubUrl(profileRequest.githubUrl());

        profileRepository.save(profile);

        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

}
