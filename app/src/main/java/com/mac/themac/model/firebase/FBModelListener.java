package com.mac.themac.model.firebase;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Samir on 9/11/2015.
 */
public interface FBModelListener  extends FBListener{
    void onDataChange(FBModelIdentifier identifier, FBModelObject model);
}
