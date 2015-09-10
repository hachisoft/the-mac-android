package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;

import java.util.Date;

/**
 * Created by Bryan on 9/8/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session extends FBModelObject {
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setClosure(String closure) {
        this.closure = closure;
    }
}
