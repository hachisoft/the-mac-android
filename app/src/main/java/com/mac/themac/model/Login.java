package com.mac.themac.model;

import com.firebase.client.AuthData;
import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 8/31/2015.
 */
public class Login extends Container {

    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    public enum FirebaseFieldName {
        created, isNotProvisioned, loginId, provider, user
    };

    private static Map<String, Field.FirebaseSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FirebaseSupportedTypes> aMap = new HashMap<String, Field.FirebaseSupportedTypes>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FirebaseFieldName.created.name(), Field.FirebaseSupportedTypes.Date);
        aMap.put(FirebaseFieldName.provider.name(), Field.FirebaseSupportedTypes.String);
        aMap.put(FirebaseFieldName.isNotProvisioned.name(), Field.FirebaseSupportedTypes.Boolean);
        aMap.put(FirebaseFieldName.user.name(), Field.FirebaseSupportedTypes.String);
        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };

    //Add all firebase object(containers of other fields) keys here
    public enum FirebaseContainerName {

    };

    //Add all firebase linked object(Linked by firebase Ids) keys here
    public enum FirebaseLinkName{
        user
    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public String getProvider() {
        return (String)fieldValue(FirebaseFieldName.provider.name());
    }

    public Date getCreated() {
        return (Date)fieldValue(FirebaseFieldName.created.name());
    }

    public Boolean getIsNotProvisioned(){
        return (Boolean)fieldValue(FirebaseFieldName.isNotProvisioned.name());
    }

    public String getUser(){
        return (String)fieldValue(FirebaseFieldName.user.name());
    }
    /////////////////////////////////////////////////////////
    private String mId;
    public Login(AuthData authData) {

        super();
        mId = authData.getUid();

        //Handle field populations
        setFieldValue(FirebaseFieldName.provider.name(), authData.getProvider());
        setFieldValue(FirebaseFieldName.created.name(), new Date());
        setFieldValue(FirebaseFieldName.isNotProvisioned.name(), true);

        _setEdited();

    }

    public String id() {
        return mId;
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
