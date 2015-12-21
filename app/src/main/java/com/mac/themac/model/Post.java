package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post extends FBModelObject {
    public Post() {
    }

    @Override
    public void loadLinkedObjects() {

    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {


    }

    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }
}
