package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "specialisations")
public class Specialisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "specialisation")
    @JsonIgnoreProperties("specialisation")
    private List<User> teachers;

    @OneToMany(mappedBy = "specialisation")
    @JsonIncludeProperties({"id"})
    private List<Cohort> cohorts;

    public Specialisation(String name) {
        this.name = name;
    }
}
