package com.mac.themac.model.firebase;

import com.firebase.client.FirebaseError;

/**
 * Created by Samir on 9/11/2015.
 */
public interface FBModelListener {

    void onDataChange(FBModelIdentifier identifier, FBModelObject model);
    void onCancel(FBModelIdentifier identifier, FBModelObject model, FirebaseError error);
    void onChildAdded(FBModelIdentifier identifier, FBModelObject model, String prevChild);
    void onChildChanged(FBModelIdentifier identifier, FBModelObject model, String key);
    void onChildRemoved(FBModelIdentifier identifier, FBModelObject model);
    void onChildMoved(FBModelIdentifier identifier, FBModelObject model, String key);

}
