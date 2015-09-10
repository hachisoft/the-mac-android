package com.mac.themac.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;

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
}
