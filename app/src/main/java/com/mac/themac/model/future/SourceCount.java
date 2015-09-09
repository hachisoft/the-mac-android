package com.mac.themac.model.future;

import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 8/31/2015.
 */
public class SourceCount extends Container {

    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    public enum FirebaseFieldName{
        phone, tablet, pc
    };

    private static Map<String, Field.FBSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FBSupportedTypes> aMap = new HashMap<>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FirebaseFieldName.phone.name(), Field.FBSupportedTypes.Integer);
        aMap.put(FirebaseFieldName.tablet.name(), Field.FBSupportedTypes.Integer);
        aMap.put(FirebaseFieldName.pc.name(), Field.FBSupportedTypes.Integer);

        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };


    public SourceCount() {
        super();
        setFieldValue(FirebaseFieldName.phone.name(), 0);
        setFieldValue(FirebaseFieldName.tablet.name(), 0);
        setFieldValue(FirebaseFieldName.pc.name(), 0);
        setEdited();
    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public int getPhone() {
        return (int)fieldValue(FirebaseFieldName.phone.name());
    }

    public int getTablet() {
        return (int)fieldValue(FirebaseFieldName.tablet.name());
    }

    public int getpc() {
        return (int)fieldValue(FirebaseFieldName.pc.name());
    }
    /////////////////////////////////////////////////////////

    public void incrementCount(FirebaseFieldName providerType){
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
