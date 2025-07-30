package com.booleanuk.cohorts.controllers;

import com.booleanuk.cohorts.models.DeliveryLog;
import com.booleanuk.cohorts.payload.response.DeliveryLogListResponse;
import com.booleanuk.cohorts.payload.response.DeliveryLogResponse;
import com.booleanuk.cohorts.payload.response.ErrorResponse;
import com.booleanuk.cohorts.payload.response.Response;
import com.booleanuk.cohorts.repository.DeliveryLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("logs")
public class DeliveryLogController {
    @Autowired
    private DeliveryLogRepository logRepository;

    @GetMapping
    public ResponseEntity<DeliveryLogListResponse> getAllLogs() {
        DeliveryLogListResponse logListResponse = new DeliveryLogListResponse();
        logListResponse.set(this.logRepository.findAll());
        return ResponseEntity.ok(logListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response> getLogById(@PathVariable int id) {
        DeliveryLog log = this.logRepository.findById(id).orElse(null);
        if (log == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        DeliveryLogResponse logResponse = new DeliveryLogResponse();
        logResponse.set(log);
        return ResponseEntity.ok(logResponse);
    }
}
