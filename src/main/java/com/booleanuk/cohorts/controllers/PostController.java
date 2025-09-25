package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Author;
import com.booleanuk.cohorts.models.Post;
import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.PostListResponse;
import com.booleanuk.cohorts.payload.response.PostResponse;
import com.booleanuk.cohorts.payload.response.Response;
import com.booleanuk.cohorts.repository.PostRepository;
import com.booleanuk.cohorts.repository.ProfileRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public ResponseEntity<?> getAllPosts(){
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        PostListResponse response = new PostListResponse();
        response.set(posts);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPostById(@PathVariable int id) {
        ErrorResponse error = new ErrorResponse();
        error.set("not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
