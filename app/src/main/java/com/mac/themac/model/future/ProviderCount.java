package com.mac.themac.model.future;

import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 7/20/2015.
 */
public class ProviderCount extends Container {

    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    public enum FBFieldName {
        facebook, google, twitter
    };

    private static Map<String, Field.FBSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FBSupportedTypes> aMap = new HashMap<>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FBFieldName.facebook.name(), Field.FBSupportedTypes.Integer);
        aMap.put(FBFieldName.google.name(), Field.FBSupportedTypes.Integer);
        aMap.put(FBFieldName.twitter.name(), Field.FBSupportedTypes.Integer);

        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };


    public ProviderCount() {
        super();
        setFieldValue(FBFieldName.facebook.name(), 0);
        setFieldValue(FBFieldName.google.name(), 0);
        setFieldValue(FBFieldName.twitter.name(), 0);
        setEdited();
    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public int getTwitter() {
        return (int)fieldValue(FBFieldName.twitter.name());
    }

    public int getFacebook() {
        return (int)fieldValue(FBFieldName.facebook.name());
    }

    public int getGoogle() {
        return (int)fieldValue(FBFieldName.google.name());
    }
    /////////////////////////////////////////////////////////

    public void incrementCount(FBFieldName providerType){
        int currentCount = (int)fieldValue(providerType.name());
        setFieldValue(providerType.name(), currentCount + 1, FirebaseFieldTypeMap.get(providerType.name()));
    }

    @Override
    protected Map<String, Field.FBSupportedTypes> fieldTypeMap() {
       return FirebaseFieldTypeMap;
    }

    @Override
    protected Field.FBSupportedTypes fieldType(String fieldName) {
        return FirebaseFieldTypeMap.get(fieldName);
    }
}
