package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Cohort;
import lombok.Getter;

import java.util.List;

@Getter
public class CohortListData extends Data<List<Cohort>> {
    protected List<Cohort> cohorts;

    @Override
    public void set(List<Cohort> cohorts) {
        this.cohorts = cohorts;
    }
}
