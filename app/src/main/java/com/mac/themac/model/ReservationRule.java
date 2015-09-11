package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationRule extends FBModelObject{

    public long weekdayPlayBegins;
    public long saturdayPlayBegins;
    public long sundayPlayBegins;
    public long weekdayPlayEnds;
    public long saturdayPlayEnds;
    public long sundayPlayEnds;

    public long sessionLength; //minutes
    // maxPlayers: DS.attr('number'), squash singles - max three players
    public long generalWindowLength; // number of days into the future you can reserve
    public long advancedWindowLength; // number of days they can reserve beyond the generalReservation window
    public boolean allowBackToBack; // allowing back to back sessions
    public long frequency;
    public long allowed; //-1:unlimited 0:none, 1:1 ...
    public long allowedPerDay; //-2:varies -1:unlimited 0:none, 1:1 ...
    public long guestAllowed;
    public long reservationChangeDeadline;
    public long timeRegistrationOpens;
    public long timeRegistrationCloses;
    public String interest;

    @JsonIgnore
    public Interest linkedInterest;

    public ReservationRule() {
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(interest != null && !interest.isEmpty()) {
            loadLinkedObject(Interest.class, FirebaseHelper.FBRootContainerNames.interests, interest);
        }
    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType,
                                   FBModelObject modelObject, int secondaryIdentifier) {

        if(targetObjectType.equals(Interest.class) &&
                modelObject instanceof Interest) {
            linkedInterest = (Interest) modelObject;
        }
    }
}
