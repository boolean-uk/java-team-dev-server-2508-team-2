package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.MessageResponse;
import com.booleanuk.cohorts.repository.ProfileRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    PasswordEncoder encoder;

    private record profileRequest(String firstName, String lastName, String phone, String githubUrl, String bio, String email, String password){}

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<?> registerProfile(@PathVariable int userId, @RequestBody profileRequest profileRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        }

        if (profileRequest.email() != null) {
            String email = profileRequest.email();
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (email.isBlank() || !email.matches(emailRegex)) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid email"));
            }
            if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
                return ResponseEntity.status(409).body(new ErrorResponse("Email is already in use"));
            }
            user.setEmail(email);
        }
        if (profileRequest.password() != null) {
            String password = profileRequest.password();
            if (password.length() < 8 ||
                    !password.matches(".*[A-Z].*") ||
                    !password.matches(".*\\d.*") ||
                    !password.matches(".*[!@#$%^&*()].*")) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid password"));
            }
            user.setPassword(encoder.encode(password));
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
