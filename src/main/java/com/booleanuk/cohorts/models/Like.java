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
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("users")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties("posts")
    private Post post;
}
