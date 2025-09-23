package com.booleanuk.cohorts.repository;

import com.booleanuk.cohorts.models.Cohort;
import com.booleanuk.cohorts.models.DeliveryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryLogRepository extends JpaRepository<DeliveryLog, Integer> {
    List<DeliveryLog> findAllByCohort(Cohort cohort);
}
