package com.mac.themac.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event extends FBModelObject{

    boolean allowGuests;
    boolean allowJuniors;
    boolean allowWaitlist;
    long capacity;
    String daysOfWeek;
    String description;
    Date endDate;
    String interest;
    boolean isPromoted;
    long nsEventId;
    long nsEventTypeId;
    long nsLocationId;
    Date startDate;
    String status;
    String title;
    String number;
    String imageId;
    String billingId;
    HashMap<String, Boolean> sessions;
    HashMap<String, Boolean> fees;
    HashMap<String, Boolean> interests;
    HashMap<String, Boolean> registrations;

    @JsonIgnore
    public List<FBModelObject> linkedSessions;
    @JsonIgnore
    public List<FBModelObject> linkedFees;
    @JsonIgnore
    public List<FBModelObject> linkedInterests;
    @JsonIgnore
    public List<FBModelObject> linkedRegistrations;

    public Event() {}

    public boolean getAllowGuests() {
        return allowGuests;
    }

    public boolean getAllowJuniors() {
        return allowJuniors;
    }

    public boolean getAllowWaitlist() {
        return allowWaitlist;
    }

    public long getCapacity() {
        return capacity;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public String getDescription() {
        return description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getInterest() {
        return interest;
    }

    public boolean getIsPromoted() {
        return isPromoted;
    }

    public long getNsEventId() {
        return nsEventId;
    }

    public long getNsEventTypeId() {
        return nsEventTypeId;
    }

    public long getNsLocationId() {
        return nsLocationId;
    }

    public HashMap<String, Boolean> getSessions() {
        return sessions;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getNumber() {
        return number;
    }

    public HashMap<String, Boolean> getFees() {
        return fees;
    }

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {

        if(interests == null){
            interests = new HashMap<String, Boolean>();
        }
        if (interests != null) {
            loadLinkedObjects(Interest.class, FirebaseHelper.FBRootContainerNames.interests,
                    interests, linkedInterests);
        }

        if(registrations == null){
            registrations = new HashMap<String, Boolean>();
        }
        if(registrations != null){
            loadLinkedObjects(Registration.class, FirebaseHelper.FBRootContainerNames.registrations,
                    registrations, linkedRegistrations);
        }

        if(sessions == null){
            sessions = new HashMap<String, Boolean>();
        }
        if(sessions != null){
            loadLinkedObjects(Session.class, FirebaseHelper.FBRootContainerNames.sessions,
                    sessions, linkedSessions);
        }

        if(fees == null){
            fees = new HashMap<String, Boolean>();
        }
        if(fees != null){
            loadLinkedObjects(Fee.class, FirebaseHelper.FBRootContainerNames.fees,
                    fees, linkedFees);
        }

    }
}
