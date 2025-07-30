package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("users")
    private User user;

    @Transient
    private Author author;

    public Post(int id) {
        this.id = id;
    }

    public Post(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public Post(Author author, String content) {
        this.author = author;
        this.content = content;
    }


}
