package com.mac.themac.model;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.mac.themac.model.firebase.Container;
import com.mac.themac.model.firebase.Field;
import com.mac.themac.utility.FirebaseHelper;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samir on 8/31/2015.
 */
public class Login extends Container {

    //Add all firebase field(keyvaluepair) keys here (no object/container, only fields)
    public enum FBFieldName {
        created, isNotProvisioned, loginId, provider, user
    };

    private static Map<String, Field.FBSupportedTypes> FBFieldTypeMap;
    static {
        Map<String, Field.FBSupportedTypes> aMap = new HashMap<String, Field.FBSupportedTypes>();

        //Add all firebase field Type mappings here (no object/container, only fields)
        aMap.put(FBFieldName.created.name(), Field.FBSupportedTypes.Date);
        aMap.put(FBFieldName.provider.name(), Field.FBSupportedTypes.String);
        aMap.put(FBFieldName.isNotProvisioned.name(), Field.FBSupportedTypes.Boolean);
        aMap.put(FBFieldName.user.name(), Field.FBSupportedTypes.String);
        FBFieldTypeMap = Collections.unmodifiableMap(aMap);
    };

    //Add all firebase object(containers of other fields) keys here
    public enum FBDirectContainerName {

    };

    //Add all firebase linked object(Linked by firebase Ids) keys here
    public enum FBLinkedContainerName {
        user
    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public String getProvider() {
        return (String)fieldValue(FBFieldName.provider.name());
    }

    public Date getCreated() {
        return (Date)fieldValue(FBFieldName.created.name());
    }

    public Boolean getIsNotProvisioned(){
        return (Boolean)fieldValue(FBFieldName.isNotProvisioned.name());
    }

    public String getUser(){
        return (String)fieldValue(FBFieldName.user.name());
    }
    /////////////////////////////////////////////////////////
    private String mId;
    public Login(AuthData authData, Firebase firebaseRef) {

        super(firebaseRef);
        mId = authData.getUid();

        //Handle field populations
        setFieldValue(FBFieldName.provider.name(), authData.getProvider());
        setFieldValue(FBFieldName.created.name(), new Date());
        setFieldValue(FBFieldName.isNotProvisioned.name(), true);

        //Handle direct container populations

        //Handle linked container populations
        mLinkedContainers.put(FBLinkedContainerName.user.name(), new User(authData, null));

        setEdited();

    }

    public String id() {
        return mId;
    }

    public User linkedUser(){
        return (User) mLinkedContainers.get(FBLinkedContainerName.user.name());
    }

    @Override
    protected Map<String, Field.FBSupportedTypes> fieldTypeMap() {
        return FBFieldTypeMap;
    }

    @Override
    protected Field.FBSupportedTypes fieldType(String fieldName) {
        return FBFieldTypeMap.get(fieldName);
    }
}
