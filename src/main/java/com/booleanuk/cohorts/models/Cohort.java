package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "cohorts")
public class Cohort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    String name;

    @Column
    @Temporal(TemporalType.DATE)
    private java.util.Date startDate;

    @Column
    @Temporal(TemporalType.DATE)
    private java.util.Date endDate;

    @ManyToOne
    @JoinColumn(name = "specialisation_id", nullable = false)
    @JsonIncludeProperties({"id", "name"})
    private Specialisation specialisation;

    @OneToMany(mappedBy = "cohort")
    @JsonIncludeProperties({"id"})
    private List<User> students;

    public Cohort(int id) {
        this.id = id;
    }

    public Cohort(Specialisation specialisation) {
        this.specialisation = specialisation;
    }
}
