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

    Date dateReserved;
    String firstName;
    boolean hasGuest;
    String interest;
    boolean isAdvRes;
    boolean isJunior;
    String lastName;
    String location;
    String memberNumber;
    String name;
    String note;
    long nsReservationId;
    String reservationUser;
    String reservingUser;
    String session;
    String status;
    String type;
    boolean wantsPartner;

    @JsonIgnore
    public User linkedReservingUser;
    @JsonIgnore
    public User linkedReservationUser;
    @JsonIgnore
    public Session linkedSession;
    @JsonIgnore
    public Location linkedLocation;

    public Reservation(){}

    public Date getDateReserved() {
        return dateReserved;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean getHasGuest() {
        return hasGuest;
    }

    public String getInterest() {
        return interest;
    }

    public boolean getIsAdvRes() {
        return isAdvRes;
    }

    public boolean getIsJunior() {
        return isJunior;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocation() {
        return location;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public long getNsReservationId() {
        return nsReservationId;
    }

    public String getReservationUser() {
        return reservationUser;
    }

    public String getReservingUser() {
        return reservingUser;
    }

    public String getSession() {
        return session;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public boolean getWantsPartner() {
        return wantsPartner;
    }

    public void setDateReserved(Date dateReserved) {
        this.dateReserved = dateReserved;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setHasGuest(boolean hasGuest) {
        this.hasGuest = hasGuest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setIsAdvRes(boolean isAdvRes) {
        this.isAdvRes = isAdvRes;
    }

    public void setIsJunior(boolean isJunior) {
        this.isJunior = isJunior;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setNsReservationId(long nsReservationId) {
        this.nsReservationId = nsReservationId;
    }

    public void setReservationUser(String reservationUser) {
        this.reservationUser = reservationUser;
    }

    public void setReservingUser(String reservingUser) {
        this.reservingUser = reservingUser;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWantsPartner(boolean wantsPartner) {
        this.wantsPartner = wantsPartner;
    }

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
