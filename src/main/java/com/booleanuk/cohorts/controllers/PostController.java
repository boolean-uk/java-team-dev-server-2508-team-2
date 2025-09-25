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

//    @GetMapping
//    public ResponseEntity<?> getAllPosts() {
//        PostListResponse postListResponse = new PostListResponse();
//        User user = this.userRepository.findById(1).orElse(null);
//        if (user == null) {
//            ErrorResponse error = new ErrorResponse();
//            error.set("not found");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//        Profile profile = this.profileRepository.findById(user.getId()).orElse(null);
//        if (profile == null) {
//            ErrorResponse error = new ErrorResponse();
//            error.set("not found");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//        Author author = new Author(user.getId(), user.getCohort().getId(), profile.getFirstName(),
//                profile.getLastName(), user.getEmail(), profile.getBio(), profile.getGithubUrl());
//        List<Post> posts = new ArrayList<>();
//        Post post1 = this.postRepository.findById(1).orElse(null);
//        if (post1 == null){
//            ErrorResponse error = new ErrorResponse();
//            error.set("not found");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//        post1.setAuthor(author);
//        post1.setContent("Hello world!!");
//        posts.add(post1);
//        Post post2 = this.postRepository.findById(2).orElse(null);
//        if (post2 == null){
//            ErrorResponse error = new ErrorResponse();
//            error.set("not found");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//        post2.setAuthor(author);
//        post2.setContent("Hello from the void!!");
//        posts.add(post2);
//        postListResponse.set(posts);
//        return ResponseEntity.ok(postListResponse);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPostById(@PathVariable int id) {
        ErrorResponse error = new ErrorResponse();
        error.set("not found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
