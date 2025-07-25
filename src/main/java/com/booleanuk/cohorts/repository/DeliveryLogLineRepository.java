package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.DeliveryLogLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryLogLineRepository extends JpaRepository<DeliveryLogLine, Integer> {
}
