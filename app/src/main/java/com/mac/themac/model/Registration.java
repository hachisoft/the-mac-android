package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/10/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Registration extends FBModelObject{

    public String firstName;
    public String lastName;
    public String memberNumber;//id of the registeringUser
    public String comments;
    public Boolean isGuest;
    public Boolean isJunior;
    public Boolean isOnWaitlist;
    public String status;
    public String registeringUser;
    public String registeredUser;
    public String fee;
    public String event;

    @JsonIgnore
    public User linkedRegisteringUser;
    @JsonIgnore
    public User linkedRegisteredUser;
    @JsonIgnore
    public Fee linkedFee;
    @JsonIgnore
    public Event linkedEvent;

    public Registration() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if( registeredUser != null && !registeredUser.isEmpty()) {
            loadLinkedObject(User.class, FirebaseHelper.FBRootContainerNames.users, registeredUser, 0);
        }

        if(registeringUser != null && !registeringUser.isEmpty()) {
            loadLinkedObject(User.class, FirebaseHelper.FBRootContainerNames.users, registeringUser, 1);
        }

        if(fee != null && !fee.isEmpty()) {
            loadLinkedObject(Fee.class, FirebaseHelper.FBRootContainerNames.fees, fee);
        }

        if(event != null && !event.isEmpty()) {
            loadLinkedObject(Event.class, FirebaseHelper.FBRootContainerNames.events, event);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType,
                                   FBModelObject modelObject, int secondaryIdentifier) {

        if(targetObjectType.equals(User.class) &&
                modelObject instanceof User) {

            if(secondaryIdentifier ==0 ){
                linkedRegisteredUser = (User) modelObject;
            }
            else if(secondaryIdentifier == 1){
                linkedRegisteringUser = (User) modelObject;
            }

        }
        else if(targetObjectType.equals(Fee.class) &&
                modelObject instanceof Fee) {
            linkedFee = (Fee) modelObject;
        }
        else if(targetObjectType.equals(Event.class) &&
                modelObject instanceof Event) {
            linkedEvent = (Event) modelObject;
        }
    }
}
