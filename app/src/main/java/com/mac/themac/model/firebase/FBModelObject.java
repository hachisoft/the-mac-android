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
import com.mac.themac.utility.FirebaseHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FBModelObject implements FBModelListener{

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
                                     List<FBModelObject> linkedModels){

        if (keyHashMap != null) {

            final FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
            linkedModels.clear();
            for (String key : keyHashMap.keySet()) {

                fbHelper.SubscribeToModelUpdates(this, new FBModelIdentifier(targetObjectType, linkedModels), key);
                /*Firebase fbRef = fbHelper.getRootKeyedObjectRef(containerName, key);
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
                });*/
            }
        }
    }

    @JsonIgnore
    protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject){

    }

    @JsonIgnore
    protected void loadLinkedObject(final Class<? extends FBModelObject> targetObjectType,
                                    FirebaseHelper.FBRootContainerNames containerName,
                                    String key) {
        loadLinkedObject(new FBModelIdentifier(targetObjectType), containerName,key);
    }

    @JsonIgnore
    protected void loadLinkedObject(final FBModelIdentifier fbModelIdentifier,
                                     FirebaseHelper.FBRootContainerNames containerName,
                                     String key){

        if (key != null) {

            final FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
            fbHelper.SubscribeToModelUpdates(this, fbModelIdentifier, key);
            /*Firebase fbRef = fbHelper.getRootKeyedObjectRef(containerName, key);

            fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Object modelObject = dataSnapshot.getValue(fbModelIdentifier.getIntendedClass());
                        if(modelObject instanceof FBModelObject) {
                            ((FBModelObject) modelObject).FBKey = dataSnapshot.getKey();
                            setLinkedObject(fbModelIdentifier, (FBModelObject) modelObject);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });*/
        }
    }

    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject modelObject) {

        if(identifier.getPayload() == null) {//Update for parent(this) model object
            setLinkedObject(identifier, (FBModelObject) modelObject);
        }
        else{ //Update for child(linked) model object
            if(identifier.getPayload() instanceof List){
                List<FBModelObject> linkedModels = (List<FBModelObject>)identifier.getPayload();
                linkedModels.add(modelObject);
            }
        }
    }

    @Override
    public void onCancel(FBModelIdentifier identifier, FirebaseError error) {

    }

    @Override
    public void onNullData(FBModelIdentifier identifier, String key) {

    }

    @Override
    public void onException(Exception x) {

    }
}
