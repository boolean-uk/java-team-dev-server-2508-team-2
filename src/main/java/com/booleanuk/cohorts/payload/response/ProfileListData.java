package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Profile;
import com.booleanuk.cohorts.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;

import java.util.List;
@Getter
public class ProfileListData extends Data<List<Profile>> {
    @JsonView(Views.basicProfileInfo.class)
    protected List<Profile> profiles;

    @Override
    public void set(List<Profile> profileList ){this.profiles = profileList;}
}
