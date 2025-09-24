package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.Specialisation;
import com.booleanuk.cohorts.payload.response.DataResponse;
import com.booleanuk.cohorts.payload.response.Response;
import com.booleanuk.cohorts.repository.SpecialisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/specialisations")
public class SpecialisationController {
    @Autowired
    SpecialisationRepository specialisationRepository;

    @GetMapping
    public ResponseEntity<Response> getAllSpecialisations() {
        DataResponse<List<Specialisation>> specialisationResponse = new DataResponse<>();
        specialisationResponse.set(this.specialisationRepository.findAll());
        return ResponseEntity.ok(specialisationResponse);
    }
}