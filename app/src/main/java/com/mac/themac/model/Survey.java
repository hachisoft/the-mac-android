package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.fragment.EventSurvey;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bryan on 12/10/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Survey extends FBModelObject {
    String event;
    String name;
    public HashMap<String, Boolean> responses;
    HashMap<String, Boolean> surveyItems;

    @JsonIgnore
    public List<SurveyItem> linkedSurveyItems = new ArrayList<>();

    public Survey() {
    }

    @Override
    public void loadLinkedObjects() {
        if(surveyItems == null)
            surveyItems = new HashMap<>();
        loadLinkedObjects(SurveyItem.class, FirebaseHelper.FBRootContainerNames.surveyItems,
                surveyItems, linkedSurveyItems);
    }

    @Override
    public void resetLinkedObjects() {
        linkedSurveyItems.clear();
    }

    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }

    public String getEvent() {
        return event;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Boolean> getResponses() {
        return responses;
    }

    public HashMap<String, Boolean> getSurveyItems() {
        return surveyItems;
    }
}
