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
public class Location extends Container {
    public enum FirebaseFieldName {
        interest, name, nsCourseLocationId, nsLocationId;
    }

    private static Map<String, Field.FirebaseSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FirebaseSupportedTypes> aMap = new HashMap<String, Field.FirebaseSupportedTypes>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FirebaseFieldName.name.name(), Field.FirebaseSupportedTypes.String);
        aMap.put(FirebaseFieldName.nsCourseLocationId.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.nsLocationId.name(), Field.FirebaseSupportedTypes.Long);
        aMap.put(FirebaseFieldName.interest.name(), Field.FirebaseSupportedTypes.String);
        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };

    //Add all firebase object(containers of other fields) keys here
    public enum FirebaseContainerName {
        sessions;
    };

    public String getName(){
        return (String) fieldValue(FirebaseFieldName.name.name());
    }

    public long getNsCourseLocationId(){
        return (long) fieldValue(FirebaseFieldName.nsCourseLocationId.name());
    }

    public long getNsLocationId(){
        return (long) fieldValue(FirebaseFieldName.nsLocationId.name());
    }

    public String getInterest(){
        return (String) fieldValue(FirebaseFieldName.interest.name());
    }

    String key;

    public Location(DataSnapshot dataSnapshot){
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
