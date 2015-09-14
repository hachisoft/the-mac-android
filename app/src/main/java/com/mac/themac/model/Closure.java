package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bryan on 9/8/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Closure extends FBModelObject{

    String daysOfWeek;
    String description;
    Date endDate;
    String interest;
    long nsClosureId;
    Date startDate;
    String status;
    String title;
    HashMap<String, Boolean> interests;
    HashMap<String, Boolean> sessions;

    @JsonIgnore
    public List<FBModelObject> linkedInterests;
    @JsonIgnore
    public List<FBModelObject> linkedSessions;


    public Closure(){}

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

    public long getNsClosureId() {
        return nsClosureId;
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

        if(sessions == null){
            sessions = new HashMap<String, Boolean>();
        }
        if(sessions != null){
            loadLinkedObjects(Session.class, FirebaseHelper.FBRootContainerNames.sessions,
                    sessions, linkedSessions);
        }

    }
}
