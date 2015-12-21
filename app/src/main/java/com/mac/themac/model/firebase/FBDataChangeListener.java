package com.mac.themac.model.firebase;

/**
 * Created by Samir on 12/17/2015.
 */
public interface FBDataChangeListener extends FBListener {
    void onDataChange(FBModelIdentifier identifier, FBModelObject model);
}
