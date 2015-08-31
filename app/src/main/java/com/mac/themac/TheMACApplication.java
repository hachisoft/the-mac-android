package com.mac.themac;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.android.gms.plus.Plus;
import com.mac.themac.utility.FirebaseHelper;

/**
 * Created by Samir on 6/29/2015.
 */
public class TheMACApplication extends Application {

    public static TheMACApplication theApp = null;
    public static int noAnimationNewTaskIntentFlag = Intent.FLAG_ACTIVITY_NO_ANIMATION;
    private FirebaseHelper mFirebaseHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Firebase
        Firebase.setAndroidContext(this);
        mFirebaseHelper = new FirebaseHelper( getString(R.string.firebase_url));

        //initialize Facebook
        FacebookSdk.sdkInitialize(this);

        theApp = this;
    }

    @Override
    public void onTerminate() {
        if(mFirebaseHelper.getFirebaseRef() != null)
            mFirebaseHelper.getFirebaseRef().unauth();
        super.onTerminate();
    }

    public FirebaseHelper getFirebaseHelper(){
        return mFirebaseHelper;
    }

    public static void startActivity(Activity currentActivity, Class activityClass){

        startActivity(currentActivity, activityClass, 0, true);
    }

    public static void startActivity(Activity currentActivity, Class activityClass, boolean finishCurrentActivity){

        startActivity(currentActivity, activityClass, 0, finishCurrentActivity);
    }

    public static void startActivity(Activity currentActivity, Class activityClass, int flags, boolean finishCurrentActivity){

        //If needed handle back stack for activity and tasks here
        Intent intent = new Intent(currentActivity, activityClass);
        if(flags > 0) {
            intent.addFlags(flags);
        }
        else{
            //add default flag
            intent.addFlags(noAnimationNewTaskIntentFlag);
        }

        if(finishCurrentActivity){
            currentActivity.finish();
            currentActivity.overridePendingTransition(0, 0);
        }
        currentActivity.startActivity(intent);
    }
}
