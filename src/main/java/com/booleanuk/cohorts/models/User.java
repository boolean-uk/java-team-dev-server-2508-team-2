package com.booleanuk.cohorts.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static com.booleanuk.cohorts.models.ERole.ROLE_STUDENT;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    // The AuthController uses a built-in class for Users that expects a Username, we don't use it elsewhere in the code.
    @Transient
    private String username = this.email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "cohort_id", nullable = true)
    @JsonIgnoreProperties("users")
    private Cohort cohort;

    @ManyToOne
    @JoinColumn(name = "specialisation_id", nullable = true)
    @JsonIncludeProperties({"id", "name"})
    private Specialisation specialisation;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private Set<UserExercise> userExercises = new HashSet<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, Cohort cohort) {
        this.email = email;
        this.password = password;
        this.cohort = cohort;
    }

    public boolean isStudent(){
        return roles.stream().anyMatch(r -> r.getName().equals(ROLE_STUDENT));
    }
}
