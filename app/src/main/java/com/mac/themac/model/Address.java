package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/11/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends FBModelObject {
    public String addressLine1;
    public String addressLine2;
    public String addressLine3;
    public String city;
    public String state;
    public String country;
    public String zip;
    public String company;
    public String user;

    @JsonIgnore
    public User mUser;

    public Address() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(user != null && !user.isEmpty()) {
            loadLinkedObject(User.class,
                    FirebaseHelper.FBRootContainerNames.users,
                    user, mUser);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject,User.class)) {
            mUser = (User) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects(){
        mUser = null;
    }
}
