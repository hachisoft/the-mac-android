package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelIdentifier;
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
            loadLinkedObject(new FBModelIdentifier(User.class),
                    FirebaseHelper.FBRootContainerNames.users, registeredUser,
                    linkedRegisteredUser);
        }

        if(registeringUser != null && !registeringUser.isEmpty()) {
            loadLinkedObject(new FBModelIdentifier(User.class, 1),
                    FirebaseHelper.FBRootContainerNames.users, registeringUser,
                    linkedRegisteringUser);
        }

        if(fee != null && !fee.isEmpty()) {
            loadLinkedObject(Fee.class, FirebaseHelper.FBRootContainerNames.fees, fee,
                    linkedFee);
        }

        if(event != null && !event.isEmpty()) {
            loadLinkedObject(Event.class, FirebaseHelper.FBRootContainerNames.events, event,
                    linkedEvent);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)) {
            linkedRegisteredUser = (User) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, User.class, 1)){
            linkedRegisteringUser = (User) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Fee.class)) {
            linkedFee = (Fee) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Event.class)) {
            linkedEvent = (Event) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedRegisteredUser = null;
        linkedRegisteringUser = null;
        linkedFee = null;
        linkedEvent = null;
    }
}
