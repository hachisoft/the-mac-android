package com.mac.themac.utility;

import com.firebase.client.Firebase;
import com.mac.themac.model.Login;
import com.mac.themac.model.User;

/**
 * Created by Samir on 7/3/2015.
 */
public class FirebaseHelper {

    private Firebase _firebaseRef;
    private Login _login;
    private User _loggedInUser;

    public enum FBRootContainerNames{
        logins, users, sessions, interests, events, reservations, reservationRules, registrations,
        fees, closures, locations, memberProfiles, memberProfilePublics, employeeProfiles
    }

    public FirebaseHelper(String firebaseUrl) {
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

    public Firebase getRootKeyedObjectRef(FBRootContainerNames containerName, String key){
        return _firebaseRef.child(containerName.name() + "/" + key);
    }

    public Firebase getLoginRef(String key){
        return _firebaseRef.child(FBRootContainerNames.logins.name() + "/" + key);
    }

    public Firebase getUserRef(String key){
        return _firebaseRef.child(FBRootContainerNames.users.name() + "/" + key);
    }

    public Firebase getSessionRef(String key){
        return _firebaseRef.child(FBRootContainerNames.sessions.name() + "/" + key);
    }
}
