package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;

import java.util.Date;

/**
 * Created by Bryan on 10/23/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkingProjection extends FBModelObject {
    public int anticipatedEarly;
    public int anticipatedMidDay;
    public int anticipatedLate;
    public int capacity;
    public Date date;
    public String statusEarly;
    public String statusMidDay;
    public String statusLate;

    @Override
    public void loadLinkedObjects() {

    }

    @Override
    public void resetLinkedObjects() {

    }

    @Override
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject) {

    }

    public int getAnticipatedEarly() {
        return anticipatedEarly;
    }

    public int getAnticipatedMidDay() {
        return anticipatedMidDay;
    }

    public int getAnticipatedLate() {
        return anticipatedLate;
    }

    public int getCapacity() {
        return capacity;
    }

    public Date getDate() {
        return date;
    }

    public String getStatusEarly() {
        return statusEarly;
    }

    public String getStatusMidDay() {
        return statusMidDay;
    }

    public String getStatusLate() {
        return statusLate;
    }
}
