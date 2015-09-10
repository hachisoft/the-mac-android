package com.mac.themac.model;

import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
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

        if(user != null && !user.isEmpty()) {
            loadLinkedObject(User.class, FirebaseHelper.FBRootContainerNames.users, user);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType, FBModelObject modelObject) {

        if(targetObjectType.equals(User.class) &&
                modelObject instanceof User){

            linkedUser =  (User)modelObject;

            if(linkedUser.logins == null){
                linkedUser.logins = new HashMap<String, Boolean>();
            }
            if(linkedUser.logins!= null &&
                    !linkedUser.logins.containsKey(this.FBKey)){
                linkedUser.logins.put(this.FBKey, true);

                Firebase ref = TheMACApplication.theApp.getFirebaseHelper().getUserRef(linkedUser.FBKey);
                ref.setValue(linkedUser);
            }

            linkedUser.loadLinkedObjects();
        }
    }
}
