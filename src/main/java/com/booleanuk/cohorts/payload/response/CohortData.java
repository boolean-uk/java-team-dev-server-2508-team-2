package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Cohort;
import lombok.Getter;

@Getter
public class CohortData extends Data<Cohort> {
    protected Cohort cohort;

    @Override
    public void set(Cohort cohort) {
        this.cohort = cohort;
    }
}
