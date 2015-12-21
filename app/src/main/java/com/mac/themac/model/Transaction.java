package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;


/**
 * Created by Samir on 10/12/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction extends FBModelObject {

    public enum STATUS{
        Pending, Processed, Cancelled
    }

    public Transaction() {
    }

    public String memberNumber;
    public String chargingMemberNumber;
    public String description; //event.title + fee.description
    public String details;
    public String statement;
    public Date date; //date of transaction
    public Date processedDate;
    public STATUS status;
    public float amount;
    public String user;
    public String registration;

    @JsonIgnore
    public Statement linkedStatement;
    @JsonIgnore
    public User linkedUser;
    @JsonIgnore
    public Registration linkedRegistration;

    @JsonIgnore
    @Override
    public void loadLinkedObjects() {

        if(statement != null && !statement.isEmpty()){
            loadLinkedObject(Statement.class,
                    FirebaseHelper.FBRootContainerNames.statements,
                    statement, linkedStatement);
        }

        if(user != null && !user.isEmpty()){
            loadLinkedObject(User.class,
                    FirebaseHelper.FBRootContainerNames.users,
                    user, linkedUser);
        }

        if(registration != null && !registration.isEmpty()){
            loadLinkedObject(Registration.class,
                    FirebaseHelper.FBRootContainerNames.registrations,
                    registration, linkedRegistration);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, Registration.class)) {
            linkedRegistration = (Registration) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Statement.class)) {
            linkedStatement = (Statement) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)) {
            linkedUser = (User) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {
        linkedRegistration = null;
        linkedStatement = null;
        linkedUser = null;
    }
}
