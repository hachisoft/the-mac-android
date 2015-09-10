package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;

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
    public String FBKey;
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
    public void setLinkedObjects(){

        FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();

        if(user != null && !user.isEmpty()){
            Firebase fbRef = fbHelper.getUserRef(user);
            fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        linkedUser = dataSnapshot.getValue(User.class);
                        linkedUser.FBKey = dataSnapshot.getKey();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
}
