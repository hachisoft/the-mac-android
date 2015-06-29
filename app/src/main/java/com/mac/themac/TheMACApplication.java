package com.mac.themac;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

/**
 * Created by Samir on 6/29/2015.
 */
public class TheMACApplication extends Application{

    private Firebase mFirebase = null;
    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Firebase
        Firebase.setAndroidContext(this);
        Firebase mFirebase = new Firebase("https://luminous-heat-7934.firebaseio.com/");

        //initialize Facebook
        FacebookSdk.sdkInitialize(this);
    }

    public Firebase getFirebaseRef(){
        return mFirebase;
    }
}
