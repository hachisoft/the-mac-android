package com.mac.themac.model.firebase;

/**
 * Created by Samir on 9/11/2015.
 */
public interface FBModelListener extends FBListener {
    void onDataChange(FBModelIdentifier identifier, FBModelObject model);
}
