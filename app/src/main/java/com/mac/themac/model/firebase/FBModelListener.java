package com.mac.themac.model.firebase;

import com.firebase.client.FirebaseError;

/**
 * Created by Samir on 9/11/2015.
 */
public interface FBModelListener extends FBDataChangeListener {
    void onCancel(FBModelIdentifier identifier, FirebaseError error);
    //Gets called when DataSnapshot do not have a value(dataSnapshot.exists() == false)
    void onNullData(FBModelIdentifier identifier, String key);
    void onException(Exception x);
}
