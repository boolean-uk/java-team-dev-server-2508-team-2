package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Cohort;
import com.booleanuk.cohorts.models.ERole;
import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.models.Role;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.CohortRepository;
import com.booleanuk.cohorts.repository.ProfileRepository;
import com.booleanuk.cohorts.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.booleanuk.cohorts.models.ERole.ROLE_STUDENT;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("cohorts")
public class CohortController {
    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public ResponseEntity<CohortListResponse> getAllCohorts() {
        CohortListResponse cohortListResponse = new CohortListResponse();
        cohortListResponse.set(this.cohortRepository.findAll());
        return ResponseEntity.ok(cohortListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response> getCohortById(@PathVariable int id) {
        Cohort cohort = this.cohortRepository.findById(id).orElse(null);
        if (cohort == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("cohort with the given id not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        CohortResponse cohortResponse = new CohortResponse();
        cohortResponse.set(cohort);
        return ResponseEntity.ok(cohortResponse);
    }

    @GetMapping("{id}/students")
    @JsonView(Views.basicProfileInfo.class)
    public ResponseEntity<Response> getStudentsByCohortId(@PathVariable int id) {

        Cohort cohort = this.cohortRepository.findById(id).orElse(null);
        if (cohort == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        var profiles = profileRepository.findAll();
        var students = profiles.stream().filter((p) ->
                p.getUser().getCohort() == cohort
                        &&
                        (p.getUser().getRoles()
                        .stream().anyMatch((r) -> r.getName().equals(ROLE_STUDENT)))
        ).toList();


        ProfileListResponse profileListResponse = new ProfileListResponse();
        profileListResponse.set(students);
        return ResponseEntity.ok(profileListResponse);
    }
}
