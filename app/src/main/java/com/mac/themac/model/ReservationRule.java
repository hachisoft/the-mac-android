package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;

import java.util.Date;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationRule extends FBModelObject{

    public long weekdayPlayBegins;
    public long saturdayPlayBegins;
    public long sundayPlayBegins;
    public long weekdayPlayEnds;
    public long saturdayPlayEnds;
    public long sundayPlayEnds;

    public long sessionLength; //minutes
    // maxPlayers: DS.attr('number'), squash singles - max three players
    public long generalWindowLength; // number of days into the future you can reserve
    public long advancedWindowLength; // number of days they can reserve beyond the generalReservation window
    public boolean allowBackToBack; // allowing back to back sessions
    //frequency: DS.attr('rule-frequency'),
    public long allowed; //-1:unlimited 0:none, 1:1 ...
    public long allowedPerDay; //-2:varies -1:unlimited 0:none, 1:1 ...
    public long guestAllowed;
    public long reservationChangeDeadline;
    public long timeRegistrationOpens;
    public long timeRegistrationCloses;
    public String interest;

    @JsonIgnore
    public Interest linkedInterest;

    public ReservationRule() {
    }

    public long getWeekdayPlayBegins() {
        return weekdayPlayBegins;
    }

    public long getSaturdayPlayBegins() {
        return saturdayPlayBegins;
    }

    public long getSundayPlayBegins() {
        return sundayPlayBegins;
    }

    public long getWeekdayPlayEnds() {
        return weekdayPlayEnds;
    }

    public long getSaturdayPlayEnds() {
        return saturdayPlayEnds;
    }

    public long getSundayPlayEnds() {
        return sundayPlayEnds;
    }

    public long getSessionLength() {
        return sessionLength;
    }

    public long getGeneralWindowLength() {
        return generalWindowLength;
    }

    public long getAdvancedWindowLength() {
        return advancedWindowLength;
    }

    public boolean isAllowBackToBack() {
        return allowBackToBack;
    }

    public long getAllowed() {
        return allowed;
    }

    public long getAllowedPerDay() {
        return allowedPerDay;
    }

    public long getGuestAllowed() {
        return guestAllowed;
    }

    public long getReservationChangeDeadline() {
        return reservationChangeDeadline;
    }

    public long getTimeRegistrationOpens() {
        return timeRegistrationOpens;
    }

    public long getTimeRegistrationCloses() {
        return timeRegistrationCloses;
    }

    public String getInterest() {
        return interest;
    }
}
