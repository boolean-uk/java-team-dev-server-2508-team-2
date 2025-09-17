package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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

    @ManyToOne
    @JoinColumn(name = "specialisation_id", nullable = false)
    @JsonIncludeProperties({"id", "name"})
    private Specialisation specialisation;

    public Cohort(int id) {
        this.id = id;
    }

    public Cohort(Specialisation specialisation) {
        this.specialisation = specialisation;
    }
}
