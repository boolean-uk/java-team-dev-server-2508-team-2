package com.booleanuk.cohorts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private int id;
    private int cohortId;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String githubUrl;
    private String role;

    public Author(int id, int cohortId, String firstName, String lastName, String email, String bio, String githubUrl) {
        this.id = id;
        this.cohortId = cohortId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bio = bio;
        this.githubUrl = githubUrl;
    }
}
