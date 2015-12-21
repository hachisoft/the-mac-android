package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;

import java.util.Map;

/**
 * Created by Bryan on 12/10/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SurveyItem extends FBModelObject {
    public long index;
    public String question;
    public String survey;
    public String type;
    public Map<String, String> options;

    public SurveyItem() {
    }

    @Override
    public void loadLinkedObjects() {

    }

    @Override
    public void resetLinkedObjects() {

    }

    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }
}
