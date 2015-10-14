package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vehicle extends FBModelObject {

    public String make;
    public String model;
    public String licensePlate;
    public String state;
    public String color;
    public String permitNumber;
    public Date permitIssued;
    public String user;

    @JsonIgnore
    public User linkedUser;

    public Vehicle() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(user != null && !user.isEmpty()) {
            loadLinkedObject(User.class,
                    FirebaseHelper.FBRootContainerNames.users,
                    user, linkedUser);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)) {
            linkedUser = (User) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedUser = null;

    }
}
