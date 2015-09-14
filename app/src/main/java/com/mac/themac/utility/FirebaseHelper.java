package com.mac.themac.utility;

import android.util.Pair;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mac.themac.model.Login;
import com.mac.themac.model.User;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samir on 7/3/2015.
 */
public class FirebaseHelper {

    private Firebase _firebaseRef;
    private Login _login;
    private User _loggedInUser;
    private String _fbUrl;

    /*TODO: Currently disabled/not implemented broadcast of changes to all listeners. Whenever
            broadcast is needed, will need to add here and update FBModelListener to unsubscribe a
            listner when destroyed.
            */
    private HashMap<Class<? extends FBModelObject>, HashMap<String, Pair<FBModelObject, List<FBModelListener>>>> _fbModelListenerMap = new HashMap<Class<? extends FBModelObject>, HashMap<String, Pair<FBModelObject, List<FBModelListener>>>>();
    private HashMap<Class<? extends FBModelObject>, HashMap<String, Pair<FBModelObject, List<FBChildListener>>>> _fbChildListenerMap = new HashMap<Class<? extends FBModelObject>, HashMap<String, Pair<FBModelObject, List<FBChildListener>>>>();

    public enum FBRootContainerNames{
        logins, users, sessions, interests, events, reservations, reservationRules, registrations,
        fees, closures, locations, memberProfiles, memberProfilePublics, employeeProfiles,
        addresses, vehicles, emergencyContacts, invitations, groups, rules
    }

    public FirebaseHelper(String firebaseUrl) {
        _fbUrl = firebaseUrl;
        this._firebaseRef = new Firebase(firebaseUrl);
    }

    public FirebaseHelper(Firebase _firebaseRef) {
        this._firebaseRef = _firebaseRef;
    }

    public Firebase getFirebaseRef() {
        return _firebaseRef;
    }

    public User getLoggedInUser() {
        return _loggedInUser;
    }

    public Login getLogin(){
        return _login;
    }

    public void set_firebaseRef(Firebase _firebaseRef) {
        this._firebaseRef = _firebaseRef;
    }

    public void set_login(Login login){
        this._login = login;
    }

    public void set_loggedInUser(User _loggedInUser) {
        this._loggedInUser = _loggedInUser;
    }

    public void addAuthStateListener(Firebase.AuthStateListener listener) {
        if(_firebaseRef != null){
            _firebaseRef.addAuthStateListener(listener);
        }
    }

    public void removeAuthStateListener(Firebase.AuthStateListener listener) {
        if(_firebaseRef != null){
            _firebaseRef.removeAuthStateListener(listener);
        }

    }

    public void unauth() {
        _loggedInUser = null;
        _login = null;
        if(_firebaseRef != null){
            _firebaseRef.unauth();
        }
    }

    public Firebase getRootKeyedObjectRef(FBRootContainerNames containerName, boolean createNew){
        if(createNew){
            return new Firebase(_fbUrl).child(containerName.name());
        }
        else {
            return _firebaseRef.child(containerName.name());
        }
    }

    public Firebase getRootKeyedObjectRef(FBRootContainerNames containerName){
        return getRootKeyedObjectRef(containerName, false);
    }

    public Firebase getRootKeyedObjectRef(FBRootContainerNames containerName, String key, boolean createNew){
        if(createNew){
            return new Firebase(_fbUrl).child(containerName.name() + "/" + key);
        }
        else {
            return _firebaseRef.child(containerName.name() + "/" + key);
        }
    }

    public Firebase getRootKeyedObjectRef(FBRootContainerNames containerName, String key){
        return getRootKeyedObjectRef(containerName, key, false);
    }

    public Firebase getLoginRef(String key){
        return _firebaseRef.child(FBRootContainerNames.logins.name() + "/" + key);
    }

    public Firebase getUserRef(String key){
        return _firebaseRef.child(FBRootContainerNames.users.name() + "/" + key);
    }

    public void removeCachedModel(String key, FBModelObject fbModelObject){

        if(!_fbModelListenerMap.containsKey(fbModelObject.getClass())){
            _fbModelListenerMap.put(fbModelObject.getClass(), new HashMap<String, Pair<FBModelObject, List<FBModelListener>>>());
        }

        HashMap<String, Pair<FBModelObject, List<FBModelListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelObject.getClass());
        if(keyToModelMap.containsKey(key)) {
            keyToModelMap.remove(key);
        }
    }

    public void removeModelListener(String key, FBModelObject fbModelObject, FBModelListener listener){

        if(!_fbModelListenerMap.containsKey(fbModelObject.getClass())){
            _fbModelListenerMap.put(fbModelObject.getClass(), new HashMap<String, Pair<FBModelObject, List<FBModelListener>>>());
        }

        HashMap<String, Pair<FBModelObject, List<FBModelListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelObject.getClass());
        if(keyToModelMap.containsKey(key)) {
            Pair<FBModelObject, List<FBModelListener>> modelListenerPair = keyToModelMap.get(key);
            if (modelListenerPair.first != null) {
                if (modelListenerPair.second.contains(listener))
                    modelListenerPair.second.remove(listener);
            }
        }
    }

    public void addModelListener(String key, FBModelObject fbModelObject, FBModelListener listener){

        if(!_fbModelListenerMap.containsKey(fbModelObject.getClass())){
            _fbModelListenerMap.put(fbModelObject.getClass(), new HashMap<String, Pair<FBModelObject, List<FBModelListener>>>());
        }

        HashMap<String, Pair<FBModelObject, List<FBModelListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelObject.getClass());
        if(keyToModelMap.containsKey(key)) {
            Pair<FBModelObject, List<FBModelListener>> modelListenerPair = keyToModelMap.get(key);
            if (modelListenerPair.first != null) {
                if (!modelListenerPair.second.contains(listener))
                    modelListenerPair.second.add(listener);
            }
        }
        else{
            List<FBModelListener> list = new ArrayList<FBModelListener>();
            list.add(listener);
            keyToModelMap.put(key, new Pair<FBModelObject, List<FBModelListener>>(fbModelObject, list));
        }
    }

    public void setFBModelObject(FBModelObject fbModelObject, String key, Class<? extends FBModelObject> classType){
        Firebase fbRef = null;

        if(classType.equals(Login.class)){
            if(key != null && key.length() > 0) {
                fbRef = getRootKeyedObjectRef(FBRootContainerNames.logins, key);
                fbRef.setValue(fbModelObject);
            }
        }
    }

    public void SubscribeToModelUpdates(final FBModelListener listener,
                                        final FBModelIdentifier fbModelIdentifier,
                                        final String key) throws InvalidParameterException {

        SubscribeToModelUpdates(listener, fbModelIdentifier, key, false);
    }

    public void SubscribeToModelUpdates(final FBModelListener listener,
                                        final FBModelIdentifier fbModelIdentifier,
                                        final String key,
                                        final boolean loadLinkedObjects) throws InvalidParameterException{

        if(key == null || key.length() == 0)
            throw new InvalidParameterException("key can not be empty or null.");
        if(listener == null)
            throw new InvalidParameterException("listener can not be null.");
        if(fbModelIdentifier == null)
            throw new InvalidParameterException("fbModelIdentifier can not be null.");

        if(!_fbModelListenerMap.containsKey(fbModelIdentifier.getIntendedClass())){
            _fbModelListenerMap.put(fbModelIdentifier.getIntendedClass(), new HashMap<String, Pair<FBModelObject, List<FBModelListener>>>());
        }

        final HashMap<String, Pair<FBModelObject, List<FBModelListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelIdentifier.getIntendedClass());
        if(keyToModelMap.containsKey(key)) {
            Pair<FBModelObject, List<FBModelListener>> modelListenerPair = keyToModelMap.get(key);
            if (modelListenerPair.first != null) {
                if (!modelListenerPair.second.contains(listener))
                    modelListenerPair.second.add(listener);

                listener.onDataChange(fbModelIdentifier, modelListenerPair.first);
                return; //no need to further process
            }
        }
        Firebase fbRef = null;

        if(fbModelIdentifier.getIntendedClass().equals(Login.class)){
            if(key != null && key.length() > 0)
                fbRef = getRootKeyedObjectRef(FBRootContainerNames.logins, key);
        }

        if(fbRef != null){
            fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.exists()) {
                            FBModelObject fbModelObject = dataSnapshot.getValue(fbModelIdentifier.getIntendedClass());
                            fbModelObject.FBKey = key;
                            fbModelObject.loadLinkedObjects();

                            if (!keyToModelMap.containsKey(key)) {
                                List<FBModelListener> list = new ArrayList<FBModelListener>();
                                list.add(listener);
                                keyToModelMap.put(key, new Pair<FBModelObject, List<FBModelListener>>(fbModelObject, list));
                            } else {//Theoretically, should never come here
                                Pair<FBModelObject, List<FBModelListener>> modelListenerPair = keyToModelMap.get(key);
                                modelListenerPair.second.add(listener);
                            }

                            listener.onDataChange(fbModelIdentifier, fbModelObject);

                        } else
                            listener.onNullData(fbModelIdentifier, key);
                    } catch (Exception x) {
                        listener.onException(x);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    listener.onCancel(fbModelIdentifier, firebaseError);
                }
            });
        }

    }

}
