package com.mac.themac.model;

import java.util.Date;

/**
 * Created by Bryan on 9/8/2015.
 */
public class Session {
    Date date;
    Long duration;
    String event;
    String reservation;
    String location;
    String closure;

    public Session(){}

    public Date getDate() {
        return date;
    }

    public Long getDuration() {
        return duration;
    }

    public String getEvent() {
        return event;
    }

    public String getReservation() {
        return reservation;
    }

    public String getLocation() {
        return location;
    }

    public String getClosure() {
        return closure;
    }
}
