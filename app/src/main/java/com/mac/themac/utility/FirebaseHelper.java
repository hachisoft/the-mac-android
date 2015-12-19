package com.mac.themac.utility;

import android.util.Pair;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.mac.themac.model.Address;
import com.mac.themac.model.Closure;
import com.mac.themac.model.Department;
import com.mac.themac.model.EmergencyContact;
import com.mac.themac.model.EmployeeProfile;
import com.mac.themac.model.Event;
import com.mac.themac.model.Fee;
import com.mac.themac.model.Interest;
import com.mac.themac.model.Invitation;
import com.mac.themac.model.Location;
import com.mac.themac.model.Login;
import com.mac.themac.model.MemberProfilePublic;
import com.mac.themac.model.ParkingProjection;
import com.mac.themac.model.Registration;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.ReservationAsset;
import com.mac.themac.model.ReservationRule;
import com.mac.themac.model.Rule;
import com.mac.themac.model.Session;
import com.mac.themac.model.Statement;
import com.mac.themac.model.Survey;
import com.mac.themac.model.SurveyItem;
import com.mac.themac.model.Transaction;
import com.mac.themac.model.User;
import com.mac.themac.model.Vehicle;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;

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
    private HashMap<Class<? extends FBModelObject>, HashMap<String, Pair<FBModelObject, List<FBListener>>>> _fbModelListenerMap = new HashMap<Class<? extends FBModelObject>, HashMap<String, Pair<FBModelObject, List<FBListener>>>>();

    /*TODO: When adding new container name make sure to update
    getRootKeyedObjectRef(Class<? extends FBModelObject> classType, String key) function to be able
    to get the Firebase model reference
     */
    public enum FBRootContainerNames{
        logins, users, sessions, interests, events, reservations, reservationRules, registrations,
        fees, closures, locations, memberProfilePublics, employeeProfiles,
        addresses, vehicles, emergencyContacts, invitations, rules, transactions,
        statements, departments, parkingProjections, surveys, surveyItems,
        reservationAssets
    }

    public FirebaseHelper(String firebaseUrl) {
        _fbUrl = firebaseUrl;
        this._firebaseRef = new Firebase(firebaseUrl);
    }

    public String getFBUrl(){
        return _fbUrl;
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
        _fbModelListenerMap.clear();
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
        if(key != null && key.length() > 0)
            return getRootKeyedObjectRef(containerName, key, false);
        else
            return getRootKeyedObjectRef(containerName);
    }

    public Firebase getLoginRef(String key){
        return _firebaseRef.child(FBRootContainerNames.logins.name() + "/" + key);
    }

    public Firebase getUserRef(String key){
        return _firebaseRef.child(FBRootContainerNames.users.name() + "/" + key);
    }

    public Firebase getRootKeyedObjectRef(Class<? extends FBModelObject> classType, String key){


        if (classType.equals(Login.class)) {
            return getRootKeyedObjectRef(FBRootContainerNames.logins, key);
        }
        else if(classType.equals(User.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.users, key);
        }
        else if(classType.equals(Session.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.sessions, key);
        }
        else if(classType.equals(Interest.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.interests, key);
        }
        else if(classType.equals(Event.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.events, key);
        }
        else if(classType.equals(Reservation.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.reservations, key);
        }
        else if(classType.equals(ReservationRule.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.reservationRules, key);
        }
        else if(classType.equals(Registration.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.registrations, key);
        }
        else if(classType.equals(Fee.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.fees, key);
        }
        else if(classType.equals(Closure.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.closures, key);
        }
        else if(classType.equals(Location.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.locations, key);
        }
        else if(classType.equals(MemberProfilePublic.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.memberProfilePublics, key);
        }
        else if(classType.equals(EmployeeProfile.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.employeeProfiles, key);
        }
        else if(classType.equals(Address.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.addresses, key);
        }
        else if(classType.equals(Vehicle.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.vehicles, key);
        }
        else if(classType.equals(EmergencyContact.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.emergencyContacts, key);
        }
        else if(classType.equals(Invitation.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.invitations, key);
        }
        else if(classType.equals(Rule.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.rules, key);
        }
        else if(classType.equals(Statement.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.statements, key);
        }
        else if(classType.equals(Transaction.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.transactions, key);
        }
        else if(classType.equals(Department.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.departments, key);
        }
        else if(classType.equals(ParkingProjection.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.parkingProjections, key);
        }
        else if(classType.equals(Survey.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.surveys, key);
        }
        else if(classType.equals(SurveyItem.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.surveyItems, key);
        }
        else if(classType.equals(ReservationAsset.class)){
            return getRootKeyedObjectRef(FBRootContainerNames.reservationAssets, key);
        }
        else{
            throw new InvalidParameterException("No container found for class type: " + classType.toString());
        }
    }

    public void removeCachedModel(String key, FBModelObject fbModelObject){

        if(!_fbModelListenerMap.containsKey(fbModelObject.getClass())){
            _fbModelListenerMap.put(fbModelObject.getClass(), new HashMap<String, Pair<FBModelObject, List<FBListener>>>());
        }

        HashMap<String, Pair<FBModelObject, List<FBListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelObject.getClass());
        if(keyToModelMap.containsKey(key)) {
            keyToModelMap.remove(key);
        }
    }

    public void removeModelListener(String key, FBModelObject fbModelObject, FBModelListener listener){

        if(!_fbModelListenerMap.containsKey(fbModelObject.getClass())){
            _fbModelListenerMap.put(fbModelObject.getClass(), new HashMap<String, Pair<FBModelObject, List<FBListener>>>());
        }

        HashMap<String, Pair<FBModelObject, List<FBListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelObject.getClass());
        if(keyToModelMap.containsKey(key)) {
            Pair<FBModelObject, List<FBListener>> modelListenerPair = keyToModelMap.get(key);
            if (modelListenerPair.first != null) {
                if (modelListenerPair.second.contains(listener))
                    modelListenerPair.second.remove(listener);
            }
        }
    }

    public void addModelListener(String key, FBModelObject fbModelObject, FBModelListener listener){

        if(!_fbModelListenerMap.containsKey(fbModelObject.getClass())){
            _fbModelListenerMap.put(fbModelObject.getClass(), new HashMap<String, Pair<FBModelObject, List<FBListener>>>());
        }

        HashMap<String, Pair<FBModelObject, List<FBListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelObject.getClass());
        if(keyToModelMap.containsKey(key)) {
            Pair<FBModelObject, List<FBListener>> modelListenerPair = keyToModelMap.get(key);
            if (modelListenerPair.first != null) {
                if (!modelListenerPair.second.contains(listener))
                    modelListenerPair.second.add(listener);
            }
        }
        else{
            List<FBListener> list = new ArrayList<FBListener>();
            list.add(listener);
            keyToModelMap.put(key, new Pair<FBModelObject, List<FBListener>>(fbModelObject, list));
        }
    }

    public void setFBModelValue(String key, FBModelObject fbModelObject){

        if(key != null && key.length() > 0) {

            Firebase fbRef = getRootKeyedObjectRef(fbModelObject.getClass(), key);

            if(fbRef != null)
                fbRef.setValue(fbModelObject);
        }

    }

    public void SubscribeToModelUpdates(FBModelListener listener,
                                        Class<? extends FBModelObject> classType,
                                        final String key) throws InvalidParameterException {

        SubscribeToModelUpdates(listener, new FBModelIdentifier(classType), key, false);
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
            _fbModelListenerMap.put(fbModelIdentifier.getIntendedClass(), new HashMap<String, Pair<FBModelObject, List<FBListener>>>());
        }

        final HashMap<String, Pair<FBModelObject, List<FBListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelIdentifier.getIntendedClass());
        if(keyToModelMap.containsKey(key)) {
            Pair<FBModelObject, List<FBListener>> modelListenerPair = keyToModelMap.get(key);
            if (modelListenerPair.first != null) {
                if (!modelListenerPair.second.contains(listener))
                    modelListenerPair.second.add(listener);

                listener.onDataChange(fbModelIdentifier, modelListenerPair.first);
                return; //no need to further process
            }
        }

        Firebase fbRef = getRootKeyedObjectRef(fbModelIdentifier.getIntendedClass(), key);

        if(fbRef != null){
            fbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.exists()) {
                            FBModelObject fbModelObject = dataSnapshot.getValue(fbModelIdentifier.getIntendedClass());
                            fbModelObject.FBKey = key;

                            if (loadLinkedObjects)
                                fbModelObject.loadLinkedObjects();

                            if (!keyToModelMap.containsKey(key)) {
                                List<FBListener> list = new ArrayList<FBListener>();
                                list.add(listener);
                                keyToModelMap.put(key, new Pair<FBModelObject, List<FBListener>>(fbModelObject, list));
                            } else {//Theoretically, should never come here
                                Pair<FBModelObject, List<FBListener>> modelListenerPair = keyToModelMap.get(key);
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

    public void SubscribeToChildUpdates(FBChildListener listener,
                                        Class<? extends FBModelObject> classType,
                                        FBQueryIdentifier fbQueryIdentifier) throws InvalidParameterException {

        SubscribeToChildUpdates(listener, new FBModelIdentifier(classType), fbQueryIdentifier, null, false);
    }

    public void SubscribeToChildUpdates(FBChildListener listener,
                                        FBModelIdentifier fbModelIdentifier,
                                        FBQueryIdentifier fbQueryIdentifier) throws InvalidParameterException {

        SubscribeToChildUpdates(listener, fbModelIdentifier, fbQueryIdentifier, null, false);
    }

    public void SubscribeToChildUpdates(FBChildListener listener,
                                        FBModelIdentifier fbModelIdentifier,
                                        FBQueryIdentifier fbQueryIdentifier,
                                        boolean loadLinkedObjects) throws InvalidParameterException {

        SubscribeToChildUpdates(listener, fbModelIdentifier, fbQueryIdentifier, null, loadLinkedObjects);
    }

    public void SubscribeToChildUpdates(FBChildListener listener,
                                        FBModelIdentifier fbModelIdentifier,
                                        FBQueryIdentifier fbQueryIdentifier,
                                        final String key) throws InvalidParameterException {

        SubscribeToChildUpdates(listener, fbModelIdentifier, fbQueryIdentifier, key, false);
    }

    public void SubscribeToChildUpdates(final FBChildListener listener,
                                        final FBModelIdentifier fbModelIdentifier,
                                        final FBQueryIdentifier fbQueryIdentifier,
                                        final String key,
                                        final boolean loadLinkedObjects) throws InvalidParameterException{

        if(listener == null)
            throw new InvalidParameterException("listener can not be null.");
        if(fbModelIdentifier == null)
            throw new InvalidParameterException("fbModelIdentifier can not be null.");

        if(!_fbModelListenerMap.containsKey(fbModelIdentifier.getIntendedClass())){
            _fbModelListenerMap.put(fbModelIdentifier.getIntendedClass(), new HashMap<String, Pair<FBModelObject, List<FBListener>>>());
        }

        final HashMap<String, Pair<FBModelObject, List<FBListener>>> keyToModelMap = _fbModelListenerMap.get(fbModelIdentifier.getIntendedClass());

        if(key != null && key.length()> 0 && keyToModelMap.containsKey(key)) {
            Pair<FBModelObject, List<FBListener>> childListenerPair = keyToModelMap.get(key);
            if (childListenerPair.first != null) {
                if (!childListenerPair.second.contains(listener))
                    childListenerPair.second.add(listener);

                listener.onChildAdded(fbModelIdentifier, fbQueryIdentifier, childListenerPair.first, "");
                return; //no need to further process
            }
        }

        Firebase fbRef = getRootKeyedObjectRef(fbModelIdentifier.getIntendedClass(), key);

        Query fbQuery = fbQueryIdentifier.getQuery(fbRef);

        if(fbQuery != null){
            fbQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChild) {
                    try {
                        if (dataSnapshot.exists()) {
                            FBModelObject fbModelObject = dataSnapshot.getValue(fbModelIdentifier.getIntendedClass());
                            fbModelObject.FBKey = dataSnapshot.getKey();

                            if (loadLinkedObjects)
                                fbModelObject.loadLinkedObjects();

                            if (!keyToModelMap.containsKey(fbModelObject.FBKey)) {
                                List<FBListener> list = new ArrayList<FBListener>();
                                list.add(listener);
                                keyToModelMap.put(fbModelObject.FBKey, new Pair<FBModelObject, List<FBListener>>(fbModelObject, list));
                            } else {
                                Pair<FBModelObject, List<FBListener>> modelListenerPair = keyToModelMap.get(fbModelObject.FBKey);
                                modelListenerPair.second.add(listener);
                            }

                        listener.onChildAdded(fbModelIdentifier, fbQueryIdentifier, fbModelObject, prevChild);

                        } else
                            listener.onNullData(fbModelIdentifier, dataSnapshot.getKey());
                    }catch (Exception x) {
                        listener.onException(x);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChild) {
                    try {
                        if (dataSnapshot.exists()) {
                            FBModelObject fbModelObject = dataSnapshot.getValue(fbModelIdentifier.getIntendedClass());
                            fbModelObject.FBKey = dataSnapshot.getKey();

                            if (loadLinkedObjects)
                                fbModelObject.loadLinkedObjects();

                            if (!keyToModelMap.containsKey(fbModelObject.FBKey)) {
                                List<FBListener> list = new ArrayList<FBListener>();
                                list.add(listener);
                                keyToModelMap.put(fbModelObject.FBKey, new Pair<FBModelObject, List<FBListener>>(fbModelObject, list));
                            } else {
                                Pair<FBModelObject, List<FBListener>> modelListenerPair = keyToModelMap.get(fbModelObject.FBKey);
                                modelListenerPair.second.add(listener);
                            }

                            listener.onChildChanged(fbModelIdentifier, fbQueryIdentifier, fbModelObject, prevChild);

                        } else
                            listener.onNullData(fbModelIdentifier, dataSnapshot.getKey());
                    }catch (Exception x) {
                        listener.onException(x);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    try {
                        FBModelObject fbModelObject = dataSnapshot.getValue(fbModelIdentifier.getIntendedClass());
                        listener.onChildRemoved(fbModelIdentifier, fbQueryIdentifier, fbModelObject);
                    }catch (Exception x) {
                        listener.onException(x);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChild) {

                    try {
                        FBModelObject fbModelObject = dataSnapshot.getValue(fbModelIdentifier.getIntendedClass());
                        listener.onChildMoved(fbModelIdentifier, fbQueryIdentifier, fbModelObject, prevChild);
                    }catch (Exception x) {
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
