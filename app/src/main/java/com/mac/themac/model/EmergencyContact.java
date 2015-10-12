package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmergencyContact extends FBModelObject {
    public String firstName;
    public String lastName;
    public String phone;
    public String email;
    public String user;

    @JsonIgnore
    public User linkedUser;

    public EmergencyContact() {
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
    public void resetLinkedObjects() {
        linkedUser = null;
    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)) {
            linkedUser = (User) modelObject;
        }
    }
}
