package com.mac.themac.model.firebase;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.Interest;
import com.mac.themac.utility.FirebaseHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FBModelObject {

    protected Map<String , Object> nonMappedProperties = new HashMap<String , Object>();

    @JsonIgnore
    public String FBKey;

    @JsonAnyGetter
    public Map<String , Object> any() {
        return nonMappedProperties;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        nonMappedProperties.put(name, value);
    }

    @JsonIgnore
    public void loadLinkedObjects(){

    }

    @JsonIgnore
    protected void loadLinkedObjects(final Class<? extends FBModelObject> targetObjectType,
                                     FirebaseHelper.FBRootContainerNames containerName,
                                     HashMap<String, Boolean> keyHashMap,
                                     final List<FBModelObject> linkedModels){

        if (keyHashMap != null) {

            final FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
            linkedModels.clear();
            for (String key : keyHashMap.keySet()) {

                Firebase fbRef = fbHelper.getRootKeyedObjectRef(containerName, key);

                fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Object modelObject = dataSnapshot.getValue(targetObjectType);
                            if(modelObject instanceof FBModelObject) {
                                ((FBModelObject)modelObject).FBKey = dataSnapshot.getKey();
                                linkedModels.add((FBModelObject) modelObject);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
    }

    @JsonIgnore
    protected void setLinkedObject(Class<? extends FBModelObject> targetObjectType,
                                    FBModelObject modelObject){

    }

    @JsonIgnore
    protected void loadLinkedObject(final Class<? extends FBModelObject> targetObjectType,
                                     FirebaseHelper.FBRootContainerNames containerName,
                                     String key){

        if (key != null) {

            final FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
            Firebase fbRef = fbHelper.getRootKeyedObjectRef(containerName, key);

            fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Object modelObject = dataSnapshot.getValue(targetObjectType);
                        if(modelObject instanceof FBModelObject) {
                            ((FBModelObject) modelObject).FBKey = dataSnapshot.getKey();
                            setLinkedObject(targetObjectType, (FBModelObject) modelObject);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
}
