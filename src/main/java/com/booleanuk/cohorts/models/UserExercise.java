package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_exercises", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "exercise_id"}))
public class UserExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("userExercises")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    @JsonIgnoreProperties("userExercises")
    private Exercise exercise;

    @Column(nullable = false)
    private boolean status = false;
}
