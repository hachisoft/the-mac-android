package com.mac.themac.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event extends FBModelObject {

    public boolean allowGuests;
    public boolean allowJuniors;
    public boolean allowWaitlist;
    public long capacity;
    public String daysOfWeek;
    public String description;
    public Date endDate;
    public String interest;
    public boolean isPromoted;
    public long nsEventId;
    public long nsEventTypeId;
    public long nsLocationId;
    public Date startDate;
    public String status;
    public String title;
    public String number;
    public String imageId;
    public String billingId;
    public String survey;
    public HashMap<String, Boolean> sessions;
    public HashMap<String, Boolean> fees;
    public HashMap<String, Boolean> interests;
    public HashMap<String, Boolean> registrations;

    @JsonIgnore
    public List<FBModelObject> linkedSessions = new ArrayList<FBModelObject>();;
    @JsonIgnore
    public List<Fee> linkedFees = new ArrayList<>();
    @JsonIgnore
    public List<FBModelObject> linkedInterests = new ArrayList<FBModelObject>();;
    @JsonIgnore
    public List<FBModelObject> linkedRegistrations = new ArrayList<FBModelObject>();;

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

    public String getSurvey() {
        return survey;
    }

    public HashMap<String, Boolean> getFees() {
        return fees;
    }

    public HashMap<String, Boolean> getRegistrations() {
        return registrations;
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

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {
        linkedFees.clear();
        linkedRegistrations.clear();
        linkedInterests.clear();
        linkedSessions.clear();
    }

    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }
}
