package com.mac.themac.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;
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
            loadLinkedObject(User.class, FirebaseHelper.FBRootContainerNames.users, reservationUser, 0);
        }

        if(reservingUser != null && !reservingUser.isEmpty()) {
            loadLinkedObject(User.class, FirebaseHelper.FBRootContainerNames.users, reservingUser, 1);
        }

        if(session != null && !session.isEmpty()) {
            loadLinkedObject(Session.class, FirebaseHelper.FBRootContainerNames.sessions, session);
        }

    }

    @JsonIgnore
    @Override
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType,
                                    FBModelObject modelObject, int secondaryIdentifier) {

        if(targetObjectType.equals(Location.class) &&
                modelObject instanceof Location) {
            linkedLocation = (Location) modelObject;
        }
        else if(targetObjectType.equals(User.class) &&
                modelObject instanceof User) {

            if(secondaryIdentifier ==0 ){
                linkedReservationUser = (User) modelObject;
            }
            else if(secondaryIdentifier == 1){
                linkedReservingUser = (User) modelObject;
            }

        }
        else if(targetObjectType.equals(Session.class) &&
                modelObject instanceof Session) {
            linkedSession = (Session) modelObject;
        }
    }
}
