package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    @JsonIgnoreProperties("units")
    private Unit unit;

    @OneToMany(mappedBy = "exercise")
    @JsonIgnoreProperties("exercise")
    private Set<UserExercise> userExercises = new HashSet<>();
}
