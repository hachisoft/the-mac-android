package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;

/**
 * Created by Bryan on 10/14/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department extends FBModelObject implements IDirectoryData {
    String email;
    String name;
    String phone;

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

    }

    @Override
    @JsonIgnore
    public void resetLinkedObjects() {

    }

    @Override
    @JsonIgnore
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @JsonIgnore
    public String getTitle() {
        return "Department";
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    @JsonIgnore
    public String getImg() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isDepartment() {
        return true;
    }
}
