package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeProfile extends FBModelObject implements IDirectoryData{
    public String department;
    public String email;
    public String firstName;
    public String lastName;
    public String phone;
    public String thumbId;
    public String title;

    public EmployeeProfile() {
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

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getTitle() {
        return title;
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
    public String getImg() {
        return thumbId;
    }

    @Override
    public boolean isDepartment() {
        return false;
    }
}
