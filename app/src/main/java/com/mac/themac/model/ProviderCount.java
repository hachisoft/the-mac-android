package com.mac.themac.model;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 7/20/2015.
 */
public class ProviderCount extends Container {

    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    public enum FirebaseFieldName{
        facebook, google, twitter
    };

    private static Map<String, Field.FirebaseSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FirebaseSupportedTypes> aMap = new HashMap<>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FirebaseFieldName.facebook.name(), Field.FirebaseSupportedTypes.Integer);
        aMap.put(FirebaseFieldName.google.name(), Field.FirebaseSupportedTypes.Integer);
        aMap.put(FirebaseFieldName.twitter.name(), Field.FirebaseSupportedTypes.Integer);

        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };


    public ProviderCount() {
        super();
        setFieldValue(FirebaseFieldName.facebook.name(), 0);
        setFieldValue(FirebaseFieldName.google.name(), 0);
        setFieldValue(FirebaseFieldName.twitter.name(), 0);
        _setEdited();
    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public int getTwitter() {
        return (int)fieldValue(FirebaseFieldName.twitter.name());
    }

    public int getFacebook() {
        return (int)fieldValue(FirebaseFieldName.facebook.name());
    }

    public int getGoogle() {
        return (int)fieldValue(FirebaseFieldName.google.name());
    }
    /////////////////////////////////////////////////////////

    public void incrementCount(FirebaseFieldName providerType){
        int currentCount = (int)fieldValue(providerType.name());
        setFieldValue(providerType.name(), currentCount + 1, FirebaseFieldTypeMap.get(providerType.name()));
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
