package com.mac.themac.model;

import com.firebase.client.DataSnapshot;
import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bryan on 9/1/2015.
 */
public class Location {
    String key;
    String interest;
    String name;
    long nsCourseLocationId;
    long nsLocationId;
    HashMap<String, Boolean> sessions;

    public Location(){}

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

    public HashMap<String, Boolean> getSessions() {
        return sessions;
    }
}
