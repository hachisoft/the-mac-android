package com.mac.themac.model.firebase;

import com.firebase.client.FirebaseError;

/**
 * Created by Samir on 9/11/2015.
 */
public interface FBChildListener extends FBListener{
    void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild);
    void onChildChanged(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key);
    void onChildRemoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model);
    void onChildMoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key);
    void onCancel(FBModelIdentifier identifier, FirebaseError error);
    //Gets called when DataSnapshot do not have a value(dataSnapshot.exists() == false)
    void onNullData(FBModelIdentifier identifier, String key);
    void onException(Exception x);
}
