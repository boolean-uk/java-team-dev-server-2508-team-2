package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.*;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.CohortRepository;
import com.booleanuk.cohorts.repository.ProfileRepository;
import com.booleanuk.cohorts.repository.SpecialisationRepository;
import com.booleanuk.cohorts.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static com.booleanuk.cohorts.models.ERole.ROLE_STUDENT;
import static com.booleanuk.cohorts.models.ERole.ROLE_TEACHER;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("cohorts")
public class CohortController {
    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SpecialisationRepository specialisationRepository;

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

    @GetMapping("/teachers")
    @JsonView(Views.basicProfileInfo.class)
    public ResponseEntity<Response> getTeachers() {

        var profiles = profileRepository.findAll();
        var students = profiles.stream().filter((p) ->
                        (p.getUser().getRoles()
                                .stream().anyMatch((r) -> r.getName().equals(ROLE_TEACHER)))
        ).toList();


        ProfileListResponse profileListResponse = new ProfileListResponse();
        profileListResponse.set(students);
        return ResponseEntity.ok(profileListResponse);
    }

    private record CohortRequest(String name, int specialisationId, LocalDate startDate, LocalDate endDate) {}

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody CohortRequest request) {
        Specialisation specialisation = specialisationRepository.findById(request.specialisationId()).orElse(null);
        if (specialisation == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Could not find specialisation with that id"));
        }

        Cohort cohort = new Cohort();
        cohort.setName(request.name());
        cohort.setSpecialisation(specialisation);
        cohort.setStartDate(request.startDate());
        cohort.setEndDate(request.endDate());

        Cohort savedCohort = cohortRepository.save(cohort);

        CohortResponse cohortResponse = new CohortResponse();
        cohortResponse.set(savedCohort);
        return new ResponseEntity<>(cohortResponse, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response> updateCohort(@PathVariable int id, @RequestBody CohortRequest request) {
        Cohort cohortToUpdate = cohortRepository.findById(id).orElse(null);
        if (cohortToUpdate == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Could not find cohort with that id"));
        }

        Specialisation specialisation = specialisationRepository.findById(request.specialisationId()).orElse(null);
        if (specialisation == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Could not find specialisation with that id"));
        }

        cohortToUpdate.setName(request.name());
        cohortToUpdate.setSpecialisation(specialisation);
        cohortToUpdate.setStartDate(request.startDate());
        cohortToUpdate.setEndDate(request.endDate());

        Cohort updatedCohort = cohortRepository.save(cohortToUpdate);

        CohortResponse cohortResponse = new CohortResponse();
        cohortResponse.set(updatedCohort);
        return ResponseEntity.ok(cohortResponse);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Response> delete(@PathVariable int id) {
        Cohort cohortToDelete = this.cohortRepository.findById(id).orElse(null);
        if (cohortToDelete == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Could not find cohort with that id"));
        }
        for (User student: cohortToDelete.getStudents()){
            student.setCohort(null);
        }
        this.cohortRepository.delete(cohortToDelete);
        CohortResponse cohortResponse = new CohortResponse();
        cohortResponse.set(cohortToDelete);
        return ResponseEntity.ok(cohortResponse);
    }
}
