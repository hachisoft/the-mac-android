package com.mac.themac.model;

import java.util.Date;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;

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
    HashMap<String, Boolean> sessions;
    HashMap<String, Boolean> fees;
    Date startDate;
    String status;
    String title;
    String number;
	
    @JsonIgnore
    public String FBKey;

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
}