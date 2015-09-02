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
public class Interest extends Container {
    public enum FirebaseFieldName {
        hasReservation, name, nsId, reservationRule
    }

    private static Map<String, Field.FirebaseSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FirebaseSupportedTypes> aMap = new HashMap<String, Field.FirebaseSupportedTypes>();
        aMap.put(FirebaseFieldName.hasReservation.name(), Field.FirebaseSupportedTypes.Boolean);
        aMap.put(FirebaseFieldName.name.name(), Field.FirebaseSupportedTypes.String);
        aMap.put(FirebaseFieldName.nsId.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.reservationRule.name(), Field.FirebaseSupportedTypes.String);
        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    }

    public enum FirebaseContainerName {
        reservationLocations, rules
    };

    public boolean getHasReservation(){
        return (boolean) fieldValue(FirebaseFieldName.hasReservation.name());
    }

    public String getName(){
        return (String) fieldValue(FirebaseFieldName.name.name());
    }

    public long getNsId(){
        return (long) fieldValue(FirebaseFieldName.nsId.name());
    }

    public String getReservationRule(){
        return (String) fieldValue(FirebaseFieldName.reservationRule.name());
    }

    public String key;

    public Interest (DataSnapshot dataSnapshot){
        super();
        key = dataSnapshot.getKey();
    }

    @Override
    protected Map<String, Field.FirebaseSupportedTypes> fieldTypeMap() {
        return FirebaseFieldTypeMap;
    }

    @Override
    protected Field.FirebaseSupportedTypes fieldType(String fieldName) {
        return FirebaseFieldTypeMap.get(fieldName);
    }
}
