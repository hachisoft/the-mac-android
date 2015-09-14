package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;

import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Bryan on 9/1/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location extends FBModelObject {

    public String name;
    public String description;
    public String interest;
    public String photoId;
    public HashMap<String, Boolean> sessions;
    public HashMap<String, Boolean> rules;
    public HashMap<String, Boolean> reservations;

    @JsonIgnore
    public Interest linkedInterest;
    @JsonIgnore
    public List<FBModelObject> linkedSessions;
    @JsonIgnore
    public List<FBModelObject> linkedRules;
    @JsonIgnore
    public List<FBModelObject> linkedReservations;

    public Location(){}

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(interest != null && !interest.isEmpty()) {
            loadLinkedObject(Interest.class, FirebaseHelper.FBRootContainerNames.interests, interest);
        }


        if (sessions == null) {
            sessions = new HashMap<String, Boolean>();
        }
        if (sessions != null) {

            loadLinkedObjects(Session.class, FirebaseHelper.FBRootContainerNames.sessions,
                    sessions, linkedSessions);
        }

        if(reservations == null){
            reservations = new HashMap<String, Boolean>();
        }
        if (reservations != null) {
            loadLinkedObjects(Reservation.class, FirebaseHelper.FBRootContainerNames.reservations,
                    reservations, linkedReservations);
        }

        if(rules == null){
            rules = new HashMap<String, Boolean>();
        }
        if (rules != null) {
            loadLinkedObjects(Rule.class, FirebaseHelper.FBRootContainerNames.rules,
                    rules, linkedRules);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, Interest.class)) {
            linkedInterest = (Interest) modelObject;
        }
    }

}
