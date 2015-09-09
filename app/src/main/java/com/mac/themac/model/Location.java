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
    String interes;
    String name;
    long nsCourseLocationId;
    long nsLocationId;
    public enum FirebaseFieldName {
        interest, name, nsCourseLocationId, nsLocationId;
    }

    public Location(){}
}
