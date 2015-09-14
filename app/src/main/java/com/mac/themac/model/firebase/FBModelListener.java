package com.mac.themac.model.firebase;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Samir on 9/11/2015.
 */
public interface FBModelListener {
    void onDataChange(FBModelIdentifier identifier, FBModelObject model);
    void onCancel(FBModelIdentifier identifier, FirebaseError error);
    void onNullData(FBModelIdentifier identifier, String key);
    void onException(Exception x);
}
