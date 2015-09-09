package com.mac.themac.model.future;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
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
    public enum FBFieldName {
        created, provider, name, email, isAdmin, lastLogin
    };

    private static Map<String, Field.FBSupportedTypes> FBFieldTypeMap;
    static {
        Map<String, Field.FBSupportedTypes> aMap = new HashMap<String, Field.FBSupportedTypes>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FBFieldName.created.name(), Field.FBSupportedTypes.Date);
        aMap.put(FBFieldName.provider.name(), Field.FBSupportedTypes.String);
        aMap.put(FBFieldName.name.name(), Field.FBSupportedTypes.String);
        aMap.put(FBFieldName.email.name(), Field.FBSupportedTypes.String);
        aMap.put(FBFieldName.isAdmin.name(), Field.FBSupportedTypes.Boolean);
        aMap.put(FBFieldName.lastLogin.name(), Field.FBSupportedTypes.Date);
        FBFieldTypeMap = Collections.unmodifiableMap(aMap);
    };

    //Add all firebase object(containers of other fields) keys here
    public enum FBDirectContainerName {
        providerCounts, sourceCounts
    };

    //Add all firebase linked object(Linked by firebase Ids) keys here
    public enum FBLinkedContainerName {
        dependents, reservations, transactions, memberProfile, memberProfilePublic
    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public String getProvider() {
        return (String)fieldValue(FBFieldName.provider.name());
    }

    public String getName() {
        return (String)fieldValue(FBFieldName.name.name());
    }

    public String getEmail() {
        return (String)fieldValue(FBFieldName.email.name());
    }

    public boolean getIsAdmin() {
        return (boolean)fieldValue(FBFieldName.isAdmin.name());
    }

    public Date getCreated() {
        return (Date)fieldValue(FBFieldName.created.name());
    }

    public Date getLastLogin(){
        return (Date)fieldValue(FBFieldName.lastLogin.name());
    }

    public ProviderCount getProviderCounts(){
        return (ProviderCount) mDirectContainers.get(FBDirectContainerName.providerCounts.name());
    }
    /////////////////////////////////////////////////////////

    private String mId;

    public User(AuthData authData, Firebase firebaseRef) {
        super(firebaseRef);

        mId = authData.getUid();

        //Handle field populations
        setFieldValue(FBFieldName.provider.name(), authData.getProvider());
        setFieldValue(FBFieldName.created.name(), new Date());
        setFieldValue(FBFieldName.isAdmin.name(), false);

        Map<String, Object> authKeyValueMappings = authData.getProviderData();

        if(authKeyValueMappings.containsKey("displayName")){
            setFieldValue(FBFieldName.name.name(), authKeyValueMappings.get("displayName").toString());
        }
        if(authKeyValueMappings.containsKey("email")){
            setFieldValue(FBFieldName.email.name(), authKeyValueMappings.get("email").toString());
        }

        //Handle direct container populations
        mDirectContainers.put(FBDirectContainerName.providerCounts.name(), new ProviderCount());
        mDirectContainers.put(FBDirectContainerName.sourceCounts.name(), new SourceCount());

        //Handle linked container populations


        setEdited();
    }


    public String id() {
        return mId;
    }

    @Override
    protected Map<String, Field.FBSupportedTypes> fieldTypeMap(){
        return FBFieldTypeMap;
    }

    @Override
    protected Field.FBSupportedTypes fieldType(String fieldName) {
        return FBFieldTypeMap.get(fieldName);
    }

}
