package com.mac.themac.model;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Date;
import java.util.Map;

/**
 * Created by Samir on 6/29/2015.
 */
public class User {

    private String mId;
    private String mProvider;
    private String mName;
    private String mEmail;
    private boolean mIsAdmin;
    private boolean mIsHead;
    private Date mCreated;

    public User(AuthData authData) {
        mId = authData.getUid();
        mProvider = authData.getProvider();
        mCreated = new Date();
        mIsAdmin = false;
        mIsHead = false;

        Map<String, Object> authKeyValueMappings = authData.getProviderData();

        if(authKeyValueMappings.containsKey("displayName")){
            mName = authKeyValueMappings.get("displayName").toString();
        }
        if(authKeyValueMappings.containsKey("email")){
            mEmail = authKeyValueMappings.get("email").toString();
        }

    }

    //Firebase specific "get" functions: Any public function with "get" prefix will be used
    //for generating Firebase document db key-value pair
    public String getProvider() {
        return mProvider;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public boolean getIsAdmin() {
        return mIsAdmin;
    }

    public boolean getIsHead() {
        return mIsHead;
    }

    public Date getCreated() {
        return mCreated;
    }
    /////////////////////////////////////////////////////////

    public String id() {
        return mId;
    }

    public void updateFirebaseData(Firebase loggedInUserRef, DataSnapshot dataSnapshot) {

        if(dataSnapshot.getValue() == null)//user doesn't exist, add new
            loggedInUserRef.setValue(this);
        else {//User exists, update values if any changes
            Iterable<DataSnapshot> userData = dataSnapshot.getChildren();

            //Update any existing key-mapping
            for (DataSnapshot userProp : userData) {
                switch (userProp.getKey()) {
                    case "isAdmin":
                        if (userProp.getValue() == null || !userProp.getValue().equals(this.getIsAdmin())) {
                            loggedInUserRef.child("isAdmin").setValue(this.getIsAdmin());
                        }
                        break;
                    case "isHead":
                        if (userProp.getValue() == null || !userProp.getValue().equals(this.getIsHead())) {
                            loggedInUserRef.child("isHead").setValue(this.getIsHead());
                        }
                        break;
                    case "name":
                        if (userProp.getValue() == null || !userProp.getValue().equals(this.getName())) {
                            loggedInUserRef.child("name").setValue(this.getName());
                        }
                        break;
                    case "email":
                        if ((userProp.getValue() == null || !userProp.getValue().equals(this.getEmail()))) {
                            loggedInUserRef.child("email").setValue(this.getEmail());
                        }
                        break;
                    default:
                        break;
                }
            }

            //Add new key-mapping if needed
            if(!dataSnapshot.hasChild("name")){
                loggedInUserRef.child("name").setValue(this.getName());
            }
            if(!dataSnapshot.hasChild("email")){
                loggedInUserRef.child("email").setValue(this.getEmail());
            }
        }
    }
}
