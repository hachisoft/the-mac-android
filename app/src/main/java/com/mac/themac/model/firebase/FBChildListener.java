package com.mac.themac.model.firebase;

/**
 * Created by Samir on 9/11/2015.
 */
public interface FBChildListener {
    void onChildAdded(FBModelIdentifier identifier, FBModelObject model, String prevChild);
    void onChildChanged(FBModelIdentifier identifier, FBModelObject model, String key);
    void onChildRemoved(FBModelIdentifier identifier, FBModelObject model);
    void onChildMoved(FBModelIdentifier identifier, FBModelObject model, String key);
}
