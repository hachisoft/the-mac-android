package com.mac.themac.model;

import com.firebase.client.AuthData;
import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 6/29/2015.
 */
public class User extends Container {

    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    public enum FirebaseFieldName {
        created, provider, name, email, isAdmin, lastLogin
    };

    private static Map<String, Field.FirebaseSupportedTypes> FirebaseFieldTypeMap;
    static {
        Map<String, Field.FirebaseSupportedTypes> aMap = new HashMap<String, Field.FirebaseSupportedTypes>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FirebaseFieldName.created.name(), Field.FirebaseSupportedTypes.Date);
        aMap.put(FirebaseFieldName.provider.name(), Field.FirebaseSupportedTypes.String);
        aMap.put(FirebaseFieldName.name.name(), Field.FirebaseSupportedTypes.String);
        aMap.put(FirebaseFieldName.email.name(), Field.FirebaseSupportedTypes.String);
        aMap.put(FirebaseFieldName.isAdmin.name(), Field.FirebaseSupportedTypes.Boolean);
        aMap.put(FirebaseFieldName.lastLogin.name(), Field.FirebaseSupportedTypes.Date);
        FirebaseFieldTypeMap = Collections.unmodifiableMap(aMap);
    };

    //Add all firebase object(containers of other fields) keys here
    public enum FirebaseContainerName {
        providerCounts, sourceCounts
    };

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public String getProvider() {
        return (String)fieldValue(FirebaseFieldName.provider.name());
    }

    public String getName() {
        return (String)fieldValue(FirebaseFieldName.name.name());
    }

    public String getEmail() {
        return (String)fieldValue(FirebaseFieldName.email.name());
    }

    public boolean getIsAdmin() {
        return (boolean)fieldValue(FirebaseFieldName.isAdmin.name());
    }

    public Date getCreated() {
        return (Date)fieldValue(FirebaseFieldName.created.name());
    }

    public Date getLastLogin(){
        return (Date)fieldValue(FirebaseFieldName.lastLogin.name());
    }

    public ProviderCount getProviderCounts(){
        return (ProviderCount) mDirectContainers.get(FirebaseContainerName.providerCounts.name());
    }
    /////////////////////////////////////////////////////////

    private String mId;

    public User(AuthData authData) {
        super();

        mId = authData.getUid();

        //Handle field populations
        setFieldValue(FirebaseFieldName.provider.name(), authData.getProvider());
        setFieldValue(FirebaseFieldName.created.name(), new Date());
        setFieldValue(FirebaseFieldName.isAdmin.name(), false);

        Map<String, Object> authKeyValueMappings = authData.getProviderData();

        if(authKeyValueMappings.containsKey("displayName")){
            setFieldValue(FirebaseFieldName.name.name(), authKeyValueMappings.get("displayName").toString());
        }
        if(authKeyValueMappings.containsKey("email")){
            setFieldValue(FirebaseFieldName.email.name(), authKeyValueMappings.get("email").toString());
        }

        //Handle container populations
        mDirectContainers.put(FirebaseContainerName.providerCounts.name(), new ProviderCount());
        _setEdited();
    }


    public String id() {
        return mId;
    }

    @Override
    protected Map<String, Field.FirebaseSupportedTypes> fieldTypeMap(){
        return FirebaseFieldTypeMap;
    }

    @Override
    protected Field.FirebaseSupportedTypes fieldType(String fieldName) {
        return FirebaseFieldTypeMap.get(fieldName);
    }

}
