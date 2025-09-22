package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Specialisation;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.Response;
import com.booleanuk.cohorts.payload.response.SpecialisationListResponse;
import com.booleanuk.cohorts.repository.SpecialisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/specialisations")
public class SpecialisationController {
    @Autowired
    SpecialisationRepository specialisationRepository;

    @GetMapping
    public ResponseEntity<SpecialisationListResponse> getAllSpecialisations() {
        SpecialisationListResponse specialisationListResponse = new SpecialisationListResponse();
        specialisationListResponse.set(this.specialisationRepository.findAll());
        return ResponseEntity.ok(specialisationListResponse);
    }
}
