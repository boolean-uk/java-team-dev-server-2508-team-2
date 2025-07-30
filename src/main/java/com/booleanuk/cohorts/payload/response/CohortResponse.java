package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Cohort;
import lombok.Getter;

@Getter
public class CohortResponse extends Response {
    public void set(Cohort cohort) {
        Data<Cohort> data = new CohortData();
        data.set(cohort);
        super.set(data);
    }
}
