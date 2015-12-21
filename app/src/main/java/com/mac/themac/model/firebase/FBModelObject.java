package com.mac.themac.model.firebase;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.FirebaseError;
import com.mac.themac.TheMACApplication;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Samir on 9/9/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FBModelObject implements FBModelListener {

    protected Map<String , Object> nonMappedProperties = new HashMap<String , Object>();

    @JsonIgnore
    public String FBKey;

    @JsonAnyGetter
    public Map<String , Object> any() {
        return nonMappedProperties;
    }

    @JsonIgnore
    private Map<List<? extends FBModelObject>,
                List<ModelCollectionListener>> collectionListenerMap =
                            new HashMap<List<? extends FBModelObject>, List<ModelCollectionListener>>();

    @JsonIgnore
    public void setModelUpdateListener(FBDataChangeListener listner){
        final FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
        fbHelper.SubscribeToModelUpdates(listner, this.getClass(), FBKey);
    }

    @JsonIgnore
    public void setCollectionUpdateListner(List<? extends FBModelObject> collection, ModelCollectionListener listener){
        if(collectionListenerMap.containsKey(collection)){
            List<ModelCollectionListener> list = collectionListenerMap.get(collection);
            if(!list.contains(listener)) {
                list.add(listener);
            }
        }
        else{
            List<ModelCollectionListener> list = new ArrayList<ModelCollectionListener>();
            list.add(listener);
            collectionListenerMap.put(collection, list);
        }
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        nonMappedProperties.put(name, value);
    }

    @JsonIgnore abstract public void loadLinkedObjects();

    @JsonIgnore abstract public void resetLinkedObjects();

    @JsonIgnore abstract protected void setLinkedObject(FBModelIdentifier fbModelIdentifier, FBModelObject modelObject);

    @JsonIgnore
    protected void loadLinkedObjects(final Class<? extends FBModelObject> targetObjectType,
                                     FirebaseHelper.FBRootContainerNames containerName,
                                     HashMap<String, Boolean> keyHashMap,
                                     List<? extends FBModelObject> linkedModels){


        //Only load linked objects if linkedModels is not already loaded(size=0), to avoid
        // cyclic infinite recursions. If you want to reload a map use reset() on model first
        // to clear out cached linked objects.
        if (keyHashMap != null && linkedModels.size() == 0) {

            final FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
            for (String key : keyHashMap.keySet()) {

                fbHelper.SubscribeToModelUpdates(this, new FBModelIdentifier(targetObjectType, linkedModels), key);
            }
        }
    }

    @JsonIgnore
    protected void loadLinkedObject(final Class<? extends FBModelObject> targetObjectType,
                                    FirebaseHelper.FBRootContainerNames containerName,
                                    String key, FBModelObject linkedModel) {
        loadLinkedObject(new FBModelIdentifier(targetObjectType), containerName,key, linkedModel);
    }

    @JsonIgnore
    protected void loadLinkedObject(final FBModelIdentifier fbModelIdentifier,
                                     FirebaseHelper.FBRootContainerNames containerName,
                                     String key, FBModelObject linkedModel){

        //Only load linked object if linkedModel is not already loaded(==null), to avoid
        // cyclic infinite recursions. If you want to reload a linkedModel use reset() on model first
        // to clear out cached linked model.
        if (key != null && linkedModel == null) {

            final FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
            fbHelper.SubscribeToModelUpdates(this, fbModelIdentifier, key);
        }
    }

    @JsonIgnore
    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject modelObject) {

        if(identifier.getPayload() == null) {//Update for parent(this) model object
            setLinkedObject(identifier, modelObject);
        }
        else{ //Update for child(linked) model object
            if(identifier.getPayload() instanceof List){
                List<FBModelObject> linkedModels = (List<FBModelObject>)identifier.getPayload();
                linkedModels.add((FBModelObject) modelObject);

                if(collectionListenerMap.containsKey(linkedModels)){
                    for (ModelCollectionListener listner : collectionListenerMap.get(linkedModels)) {
                        try{
                            listner.onCollectionUpdated(linkedModels, (FBModelObject)modelObject, true);
                        }
                        catch(Exception e){
                            //ignore any exception thrown from listner
                        }
                    }
                }
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
