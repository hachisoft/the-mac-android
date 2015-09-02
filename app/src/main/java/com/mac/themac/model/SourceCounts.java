package com.mac.themac.model;

import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 8/31/2015.
 */
public class SourceCounts extends Container {

    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    public enum FirebaseFieldName{

    };

    private static Map<String, Field.FBSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FBSupportedTypes> aMap = new HashMap<>();

        //Add all firebase field Type mappings here (no object/container, only fields)

        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };

    //Add all firebase object(containers of other fields) keys here
    public enum FirebaseDirectContainerName {
        android, ios
    };

    //Add all firebase linked object(Linked by firebase Ids) keys here
    public enum FirebaseLinkedContainerName{

    }

    public SourceCounts() {
        super();
        setEdited();
    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair

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
