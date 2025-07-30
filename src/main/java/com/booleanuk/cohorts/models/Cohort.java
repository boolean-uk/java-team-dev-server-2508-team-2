package com.booleanuk.cohorts.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "cohorts")
public class Cohort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Cohort(int id) {
        this.id = id;
    }
}
