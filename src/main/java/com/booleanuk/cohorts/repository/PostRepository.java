package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByOrderByCreatedAtDesc();
}
