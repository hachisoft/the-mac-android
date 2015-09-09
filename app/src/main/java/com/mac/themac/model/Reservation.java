package com.mac.themac.model;

import java.util.Date;

/**
 * Created by Bryan on 9/8/2015.
 */
public class Reservation {
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
}
