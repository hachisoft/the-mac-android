package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Bryan on 9/8/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session extends FBModelObject {
    Date date;
    Long duration;
    Boolean isEvent;
    String event;
    String reservation;
    String location;
    String closure;

    @JsonIgnore
    Location linkedLocation;
    @JsonIgnore
    Event linkedEvent;
    @JsonIgnore
    Closure linkedClosure;
    @JsonIgnore
    Reservation linkedReservation;

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

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {
        if(closure != null && !closure.isEmpty()) {
            loadLinkedObject(Closure.class, FirebaseHelper.FBRootContainerNames.closures, closure);
        }

        if(event != null && !event.isEmpty()) {
            loadLinkedObject(Event.class, FirebaseHelper.FBRootContainerNames.events, event);
        }

        if(location != null && !location.isEmpty()) {
            loadLinkedObject(Location.class, FirebaseHelper.FBRootContainerNames.locations, location);
        }

        if(reservation != null && !reservation.isEmpty()) {
            loadLinkedObject(Reservation.class, FirebaseHelper.FBRootContainerNames.reservations, reservation);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType, FBModelObject modelObject) {

        if(targetObjectType.equals(Closure.class) &&
                modelObject instanceof Closure) {
            linkedClosure = (Closure) modelObject;
        }
        else if(targetObjectType.equals(Event.class) &&
                modelObject instanceof Event) {
            linkedEvent = (Event) modelObject;
        }
        else if(targetObjectType.equals(Location.class) &&
                modelObject instanceof Location) {
            linkedLocation = (Location) modelObject;
        }
        else if(targetObjectType.equals(Reservation.class) &&
                modelObject instanceof Reservation) {
            linkedReservation = (Reservation) modelObject;
        }
    }
}
