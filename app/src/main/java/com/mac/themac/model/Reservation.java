package com.mac.themac.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation extends FBModelObject{

    public Date dateReserved;
    public String firstName;
    public boolean hasGuest;
    public String interest;
    public boolean isAdvRes;
    public boolean isJunior;
    public String lastName;
    public String location;
    public String memberNumber;
    public String name;
    public String note;
    public long nsReservationId;
    public String reservationUser;
    public String reservingUser;
    public String session;
    public String status;
    public String type;
    public boolean wantsPartner;

    @JsonIgnore
    public User linkedReservingUser;
    @JsonIgnore
    public User linkedReservationUser;
    @JsonIgnore
    public Session linkedSession;
    @JsonIgnore
    public Location linkedLocation;

    public Reservation(){}

    @Override
    @JsonIgnore
    public void loadLinkedObjects() {
        if(location != null && !location.isEmpty()) {
            loadLinkedObject(Location.class, FirebaseHelper.FBRootContainerNames.locations, location);
        }

        if( reservationUser != null && !reservationUser.isEmpty()) {
            loadLinkedObject( new FBModelIdentifier(User.class), FirebaseHelper.FBRootContainerNames.users, reservationUser);
        }

        if(reservingUser != null && !reservingUser.isEmpty()) {
            loadLinkedObject( new FBModelIdentifier(User.class, 1), FirebaseHelper.FBRootContainerNames.users, reservingUser);
        }

        if(session != null && !session.isEmpty()) {
            loadLinkedObject(Session.class, FirebaseHelper.FBRootContainerNames.sessions, session);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier,
                                    FBModelObject modelObject) {

        if(fbModelIdentifier.IsIntendedObject(modelObject, Location.class)) {
            linkedLocation = (Location) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, User.class)){
            linkedReservationUser = (User) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, User.class, 1)){
            linkedReservingUser = (User) modelObject;
        }
        else if(fbModelIdentifier.IsIntendedObject(modelObject, Session.class)) {
            linkedSession = (Session) modelObject;
        }
    }
}
