package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.fragment.EventSurvey;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;

import java.util.HashMap;

/**
 * Created by Bryan on 12/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyResponse extends FBModelObject {
    public String registration;
    public String survey;
    public HashMap<Long, String> responses;
    @JsonIgnore
    public HashMap<Long, boolean[]> multiChoiceResponses = new HashMap<>();

    @Override
    public void loadLinkedObjects() {

    }

    @Override
    public void resetLinkedObjects() {

    }

    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }

    public String getRegistration() {
        return registration;
    }

    public String getSurvey() {
        return survey;
    }

    public HashMap<Long, String> getResponses() {
        if(responses == null)
            responses = new HashMap<>();
        return responses;
    }
}
