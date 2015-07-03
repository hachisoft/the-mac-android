package com.mac.themac.utility;

import com.firebase.client.Firebase;
import com.mac.themac.model.User;

/**
 * Created by Samir on 7/3/2015.
 */
public class FirebaseHelper {

    private Firebase _firebaseRef;
    private User _loggedInUser;

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

    public void set_firebaseRef(Firebase _firebaseRef) {
        this._firebaseRef = _firebaseRef;
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
        if(_firebaseRef != null){
            _firebaseRef.unauth();
        }
    }
}
