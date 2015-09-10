package com.mac.themac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mac.themac.model.firebase.FBModelObject;

/**
 * Created by Bryan on 9/1/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location extends FBModelObject{
    String key;
    String interest;
    String name;
    long nsCourseLocationId;
    long nsLocationId;

    public String getKey() {
        return key;
    }

    public String getInterest() {
        return interest;
    }

    public String getName() {
        return name;
    }

    public long getNsCourseLocationId() {
        return nsCourseLocationId;
    }

    public long getNsLocationId() {
        return nsLocationId;
    }

    public Location(){}
}
