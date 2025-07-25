package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.DeliveryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryLogRepository extends JpaRepository<DeliveryLog, Integer> {
}
