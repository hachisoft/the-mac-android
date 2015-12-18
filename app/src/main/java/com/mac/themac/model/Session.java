package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Date;

/**
 * Created by Bryan on 9/8/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session extends FBModelObject {

    public Date date;
    public Long duration;
    public Date endDate;
    public Boolean isEvent;
    public String event;
    public String reservation;
    public String location;
    public String closure;

    @JsonIgnore
    Location linkedLocation;
    @JsonIgnore
    public Event linkedEvent;
    @JsonIgnore
    Closure linkedClosure;
    @JsonIgnore
    Reservation linkedReservation;

    public Session(){}

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {
        if(closure != null && !closure.isEmpty()) {
            loadLinkedObject(Closure.class, FirebaseHelper.FBRootContainerNames.closures,
                    closure, linkedClosure);
        }

        if(event != null && !event.isEmpty()) {
            loadLinkedObject(Event.class, FirebaseHelper.FBRootContainerNames.events, event,
                    linkedEvent);
        }

        if(location != null && !location.isEmpty()) {
            loadLinkedObject(Location.class, FirebaseHelper.FBRootContainerNames.locations,
                    location, linkedLocation);
        }

        if(reservation != null && !reservation.isEmpty()) {
            loadLinkedObject(Reservation.class, FirebaseHelper.FBRootContainerNames.reservations,
                    reservation, linkedReservation);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                   FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, Closure.class)) {
            linkedClosure = (Closure) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Event.class)) {
            linkedEvent = (Event) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Location.class)) {
            linkedLocation = (Location) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Reservation.class)) {
            linkedReservation = (Reservation) modelObject;
        }
    }

    @JsonIgnore
    @Override
    public void resetLinkedObjects() {

        linkedClosure = null;
        linkedEvent = null;
        linkedLocation = null;
        linkedReservation = null;

    }
}
