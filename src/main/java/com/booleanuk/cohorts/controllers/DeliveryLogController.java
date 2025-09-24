package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.DeliveryLog;
import com.booleanuk.cohorts.models.DeliveryLog;
import com.booleanuk.cohorts.models.Cohort;
import com.booleanuk.cohorts.models.User;
import com.booleanuk.cohorts.payload.response.*;
import com.booleanuk.cohorts.repository.DeliveryLogRepository;
import com.booleanuk.cohorts.repository.CohortRepository;
import com.booleanuk.cohorts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("logs")
public class DeliveryLogController {
    @Autowired
    private DeliveryLogRepository logRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Response> getAllLogs() {
        DataResponse<List<DeliveryLog>> logListResponse = new DataResponse<>();
        logListResponse.set(this.logRepository.findAll());
        return ResponseEntity.ok(logListResponse);
    }

    @GetMapping("/{logId}")
    public ResponseEntity<Response> getLogById(@PathVariable int logId) {
        DeliveryLog log = this.logRepository.findById(logId).orElse(null);
        if (log == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        DataResponse<DeliveryLog> logResponse = new DataResponse<>();
        logResponse.set(log);
        return ResponseEntity.ok(logResponse);
    }

    @GetMapping("cohorts/{cohortId}")
    public ResponseEntity<Response> getDeliveryLogsByCohortId(@PathVariable int cohortId) {
        Cohort cohort = this.cohortRepository.findById(cohortId).orElse(null);
        if (cohort == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Cohort with that id was not found"));
        }
        var logs = logRepository.findAllByCohort(cohort);

        DataResponse<List<DeliveryLog>> logListResponse = new DataResponse<>();
        logListResponse.set(logs);
        return ResponseEntity.ok(logListResponse);
    }

    private record DeliveryLogRequest(String title, String content, LocalDateTime date) {}

    @PostMapping("cohorts/{cohortId}")
    public ResponseEntity<Response> createDeliveryLogByCohortId(
            @PathVariable int cohortId,
            @RequestBody DeliveryLogRequest logRequest,
            Authentication authentication
    ) {
        Cohort cohort = this.cohortRepository.findById(cohortId).orElse(null);
        if (cohort == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Cohort with that id was not found"));
        }

        String email = authentication.getName();
        User author = userRepository.findByEmail(email).orElse(null);
        if (author == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("Could not get author details"));
        }

        DeliveryLog newLog = new DeliveryLog();
        newLog.setCohort(cohort);
        newLog.setAuthor(author);
        newLog.setTitle(logRequest.title());
        newLog.setContent(logRequest.content());
        newLog.setDate(logRequest.date());

        logRepository.save(newLog);

        DataResponse<DeliveryLog> logResponse = new DataResponse<>();
        logResponse.set(newLog);
        return new ResponseEntity<>(logResponse, HttpStatus.CREATED);
    }

    private record PatchLogRequest(String title, String text) {}

    @PatchMapping("{logId}")
    public ResponseEntity<Response> updateDeliveryLog(@PathVariable int logId, @RequestBody PatchLogRequest logRequest) {
        DeliveryLog logToUpdate = this.logRepository.findById(logId).orElse(null);
        if (logToUpdate == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("DeliveryLog with that id was not found"));
        }

        logToUpdate.setTitle(logRequest.title());
        logToUpdate.setContent(logRequest.text());
        logToUpdate.setEdited(true);

        logRepository.save(logToUpdate);

        DataResponse<DeliveryLog> logResponse = new DataResponse<>();
        logResponse.set(logToUpdate);
        return ResponseEntity.ok(logResponse);
    }

    @DeleteMapping("{logId}")
    public ResponseEntity<Response> deleteDeliveryLog(@PathVariable int logId) {
        DeliveryLog logToDelete = this.logRepository.findById(logId).orElse(null);
        if (logToDelete == null) {
            return ResponseEntity.status(404).body(new ErrorResponse("DeliveryLog with that id was not found"));
        }
        logRepository.delete(logToDelete);

        DataResponse<DeliveryLog> logResponse = new DataResponse<>();
        logResponse.set(logToDelete);
        return ResponseEntity.ok(logResponse);
    }
}
