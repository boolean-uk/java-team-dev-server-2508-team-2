package com.booleanuk.cohorts.payload.response;


import com.booleanuk.cohorts.models.Specialisation;
import lombok.Getter;

import java.util.List;

@Getter
public class SpecialisationListResponse extends Response {
    public void set(List<Specialisation> specialisation) {
        Data<List<Specialisation>> data = new SpecialisationListData();
        data.set(specialisation);
        super.set(data);
    }
}
