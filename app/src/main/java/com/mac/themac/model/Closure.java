package com.mac.themac.model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Bryan on 9/8/2015.
 */
public class Closure {
    String daysOfWeek;
    String description;
    Date endDate;
    String interest;
    long nsClosureId;
    HashMap<String, Boolean> sessions;
    Date startDate;
    String status;
    String title;

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
}
