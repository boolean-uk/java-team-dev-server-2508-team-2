package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Cohort;
import lombok.Getter;

import java.util.List;

@Getter
public class CohortListResponse extends Response {
    public void set(List<Cohort> cohorts) {
        Data<List<Cohort>> data = new CohortListData();
        data.set(cohorts);
        super.set(data);
    }
}
