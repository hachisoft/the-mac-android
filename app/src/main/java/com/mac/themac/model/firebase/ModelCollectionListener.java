package com.mac.themac.model.firebase;

import java.util.List;

/**
 * Created by Samir on 12/17/2015.
 */
public interface ModelCollectionListener {
    void onCollectionUpdated(List<? extends FBModelObject> linkedCollection, FBModelObject fbObject, boolean isAdded);
}
