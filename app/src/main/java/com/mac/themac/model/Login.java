package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.Firebase;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Login extends FBModelObject {

    public Date created;
    public Boolean isNotProvisioned = true;
    public String provider;
    public String user;

    @JsonIgnore
    public User linkedUser;

    public Login(){

    }

    public Login(String _provider){
        created = new Date();
        isNotProvisioned = true;
        provider = _provider;
    }

    public Date getCreated() {
        return created;
    }

    public Boolean getIsNotProvisioned() {
        return isNotProvisioned;
    }

    public String getProvider() {
        return provider;
    }

    public String getUser() {
        return user;
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects(){

        if(user != null && !user.isEmpty() && linkedUser == null) {
            loadLinkedObject(User.class,
                    FirebaseHelper.FBRootContainerNames.users,
                    user, linkedUser);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                    FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)){

            linkedUser =  (User)modelObject;

            TheMACApplication.theApp.getFirebaseHelper().set_loggedInUser(linkedUser);
            if(linkedUser.logins == null){
                linkedUser.logins = new HashMap<String, Boolean>();
            }
            if(linkedUser.logins!= null &&
                    !linkedUser.logins.containsKey(this.FBKey)){
                linkedUser.logins.put(this.FBKey, true);

                Firebase ref = TheMACApplication.theApp.getFirebaseHelper().getUserRef(linkedUser.FBKey);
                ref.setValue(linkedUser);

            }

            linkedUser.loadMemberProfileInterestsRegistrations();
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedUser = null;
    }
}
