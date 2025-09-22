package com.booleanuk.cohorts.payload.response;



import com.booleanuk.cohorts.models.Specialisation;
import lombok.Getter;

import java.util.List;

@Getter
public class SpecialisationListData extends Data<List<Specialisation>> {
    protected List<Specialisation> specialisations;

    @Override
    public void set(List<Specialisation> specialisations) {
        this.specialisations = specialisations;
    }
}
