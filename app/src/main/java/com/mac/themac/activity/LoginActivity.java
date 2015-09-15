package com.mac.themac.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.Login;
import com.mac.themac.model.User;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This application demos the use of the Firebase Login feature. It currently supports logging in
 * with Google, Facebook, Twitter, Email/Password, and Anonymous providers.
 * <p/>
 * The methods in this class have been divided into sections based on providers (with a few
 * general methods).
 * <p/>
 * Facebook provides its own API via the {@link com.facebook.login.widget.LoginButton}.
 * Google provides its own API via the {@link com.google.android.gms.common.api.GoogleApiClient}.
 * Twitter requires us to use a Web View to authenticate, see
 * {@link com.mac.themac.activity.TwitterOAuthActivity}
 * Email/Password is provided using {@link com.firebase.client.Firebase}
 * Anonymous is provided using {@link com.firebase.client.Firebase}
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Firebase.AuthStateListener,
        FBModelListener, FBChildListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    /* *************************************
     *              GENERAL                *
     ***************************************/
    /* TextView that is used to display information about the logged in user
    @Bind(R.id.login_status) TextView mLoggedInStatusTextView;*/

    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;

    /* A reference to the Firebase */
    private FirebaseHelper mFBHelper;

    /* Data from the authenticated user */
    private AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;

    /* *************************************
     *              FACEBOOK               *
     ***************************************/
    /* The login button for Facebook */
    @Bind(R.id.login_with_facebook) LoginButton mFacebookLoginButton;

    /* The callback manager for Facebook */
    private CallbackManager mFacebookCallbackManager;
    /* Used to track user logging in/out off Facebook */
    private AccessTokenTracker mFacebookAccessTokenTracker;


    /* *************************************
     *              GOOGLE                 *
     ***************************************/
    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;

    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
     * without waiting. */
    private boolean mGoogleLoginClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can resolve them when the user clicks
     * sign-in. */
    private ConnectionResult mGoogleConnectionResult;

    /* The login button for Google */
    @Bind(R.id.login_with_google) SignInButton mGoogleLoginButton;

    /* *************************************
     *              TWITTER                *
     ***************************************/
    public static final int RC_TWITTER_LOGIN = 2;

    @Bind(R.id.login_with_twitter) Button mTwitterLoginButton;

    /* *************************************
     *              PASSWORD               *
     ***************************************/
    @Bind(R.id.login_with_password) Button mPasswordLoginButton;
    @Bind(R.id.txtEmail) EditText mFBLoginEmail;
    @Bind(R.id.txtPassword) EditText mFBLoginPassword;

    @Bind(R.id.login_viewSwitcher) ViewSwitcher mLoginViewSwitcher;
    @Bind(R.id.logged_in_viewSwitcher) ViewSwitcher mLoggedinViewSwitcher;
    @Bind(R.id.log_in_options_viewSwitcher) ViewSwitcher mLoginOptionsViewSwitcher;
    @Bind(R.id.btnMyAccount) ToggleButton mBtnMyAccount;
    @Bind(R.id.btnBill) ToggleButton mBtnBill;
    @Bind(R.id.btnTennisCourts) ToggleButton mBtnTennis;
    @Bind(R.id.btnFind) ToggleButton mBtnFind;
    @Bind(R.id.btnMore) ToggleButton mBtnMore;
    @Bind(R.id.btnValidateMemberId) Button mBtnValidateMemberId;
    @Bind(R.id.txtMemberId) EditText mMemberId;

    @OnClick(R.id.btnMyAccount)
    public void launchMyAccount(){
        TheMACApplication.startActivity(this, MyAccount.class, false);
    }

    @OnClick(R.id.btnBill)
    public void launchBill(){
        TheMACApplication.startActivity(this, Bill.class, false);
    }

    @OnClick(R.id.btnFind)
    public void launchFind(){
        TheMACApplication.startActivity(this, FindEvents.class, false);
    }

    @OnClick(R.id.btnTennisCourts)
    public void launchTennisCourts(){
        TheMACApplication.startActivity(this, TennisCourts.class, false);
    }

    @OnClick(R.id.btnMore)
    public void launchMore(){
        TheMACApplication.startActivity(this, More.class, false);
    }

    @OnClick(R.id.btnLoginWithEmailPasswordCancel)
    public void cancelEmailPasswordLogin(){
        mLoginOptionsViewSwitcher.setDisplayedChild(mLoginOptionsViewSwitcher.indexOfChild(findViewById(R.id.login_view1)));
    }

    @OnClick(R.id.btnCancelMemberId)
    public void cancelMemberId(){
        onClickLogout();
    }


    @OnClick(R.id.btnValidateMemberId)
    public void validateMemberId(){
        mFBHelper.SubscribeToChildUpdates(this, new FBModelIdentifier(User.class),
                new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child,
                        "memberNumber",
                        FBQueryIdentifier.Qualifier.equalTo, mMemberId.getText().toString()));
        /*Firebase ref = mFBHelper.getFirebaseRef().child(FirebaseHelper.FBRootContainerNames.users.name());
        Query queryRef = ref.orderByChild("memberNumber").equalTo(mMemberId.getText().toString());
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChild) {
                Login login = mFBHelper.getLogin();
                if (dataSnapshot.exists()) {

                    User user = dataSnapshot.getValue(User.class);
                    user.FBKey = dataSnapshot.getKey();
                    user.loadLinkedObjects();

                    login.user = user.FBKey;
                    login.isNotProvisioned = false;
                    login.linkedUser = user;

                    mFBHelper.getLoginRef(login.FBKey).setValue(login);
                    mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_home)));
                } else { //User doesn't exist
                    showErrorDialog("Invalid Member Number. Please retry with valid Member Number.");
                    mMemberId.setText("");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFBHelper = ((TheMACApplication)getApplication()).getFirebaseHelper();

        /* Load the view and display it */
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        ButterKnife.bind(this);

        if(getActionBar() != null)
            getActionBar().hide();

        /* *************************************
         *              FACEBOOK               *
         ***************************************/
        /* Load the Facebook login button and set up the tracker to monitor access token changes */
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook.AccessTokenTracker.OnCurrentAccessTokenChanged");
                LoginActivity.this.onFacebookAccessTokenChange(currentAccessToken);
            }
        };

        /* *************************************
         *               GOOGLE                *
         ***************************************/
        /* Load the Google login button */
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleLoginClicked = true;
                if (!mGoogleApiClient.isConnecting()) {
                    if (mGoogleConnectionResult != null && mGoogleConnectionResult.hasResolution()) {
                        resolveSignInError();
                    } else if (mGoogleApiClient.isConnected()) {
                        getGoogleOAuthTokenAndLogin();
                    } else {
                    /* connect API now */
                        Log.d(TAG, "Trying to connect to Google API");
                        mGoogleApiClient.connect();
                    }
                }
            }
        });
        /* Setup the Google API object to allow Google+ logins */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        /* *************************************
         *                TWITTER              *
         ***************************************/
        mTwitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithTwitter();
            }
        });

        /* *************************************
         *               PASSWORD              *
         ***************************************/
        mPasswordLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginOptionsViewSwitcher.setDisplayedChild(mLoginOptionsViewSwitcher.indexOfChild(findViewById(R.id.login_view2)));
            }
        });

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Multnomah Athletic Club...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        mFBHelper.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if user logged in with Facebook, stop tracking their token
        if (mFacebookAccessTokenTracker != null) {
            mFacebookAccessTokenTracker.stopTracking();
        }

        // if changing configurations, stop tracking firebase session.
        mFBHelper.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * This method fires when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Map<String, String> options = new HashMap<String, String>();
        if (requestCode == RC_GOOGLE_LOGIN) {
            /* This was a request by the Google API */
            if (resultCode != RESULT_OK) {
                mGoogleLoginClicked = false;
            }
            mGoogleIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else if (requestCode == RC_TWITTER_LOGIN) {
            options.put("oauth_token", data.getStringExtra("oauth_token"));
            options.put("oauth_token_secret", data.getStringExtra("oauth_token_secret"));
            options.put("user_id", data.getStringExtra("user_id"));
            authWithFirebase("twitter", options);
        } else {
            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
        if (this.mAuthData != null) {
            getMenuInflater().inflate(R.menu.menu_login, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        mBtnMyAccount.setChecked(false);
        mBtnBill.setChecked(false);
        mBtnFind.setChecked(false);
        mBtnMore.setChecked(false);
        mBtnTennis.setChecked(false);
        super.onResume();
    }


    @OnClick(R.id.btnLogout)
    public void onClickLogout(){
        logout();
        mLoginOptionsViewSwitcher.setDisplayedChild(mLoginOptionsViewSwitcher.indexOfChild(findViewById(R.id.login_view1)));
        mLoginViewSwitcher.setDisplayedChild(mLoginViewSwitcher.indexOfChild(findViewById(R.id.login_view)));
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFBHelper.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
             * Facebook/Google+ after logging out of Firebase. */
            if (this.mAuthData.getProvider().equals("facebook")) {
                /* Logout from Facebook */
                LoginManager.getInstance().logOut();
            } else if (this.mAuthData.getProvider().equals("google")) {
                /* Logout from Google+ */
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
        }
    }

    /**
     * This method will attempt to authenticate a user to firebase given an oauth_token (and other
     * necessary parameters depending on the provider)
     */
    private void authWithFirebase(final String provider, Map<String, String> options) {
        if (options.containsKey("error")) {
            showErrorDialog(options.get("error"));
        } else {
            mAuthProgressDialog.show();
            if (provider.equals("twitter")) {
                // if the provider is twitter, we must pass in additional options, so use the options endpoint
                mFBHelper.getFirebaseRef().authWithOAuthToken(provider, options, new AuthResultHandler(provider));
            } else {
                // if the provider is not twitter, we just need to pass in the oauth_token
                mFBHelper.getFirebaseRef().authWithOAuthToken(provider, options.get("oauth_token"), new AuthResultHandler(provider));
            }
        }
    }

    /**
     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(final AuthData authData) {
        if (authData != null) {

            /* Hide all the login buttons*/
            mFacebookLoginButton.setVisibility(View.GONE);
            mGoogleLoginButton.setVisibility(View.GONE);
            mTwitterLoginButton.setVisibility(View.GONE);
            mPasswordLoginButton.setVisibility(View.GONE);
            //mLoggedInStatusTextView.setVisibility(View.VISIBLE);

            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("facebook")
                    || authData.getProvider().equals("google")
                    || authData.getProvider().equals("twitter")) {
                name = (String) authData.getProviderData().get("displayName");
            } else if (authData.getProvider().equals("anonymous")
                    || authData.getProvider().equals("password")) {
                name = authData.getUid();
            } else {
                Log.e(TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
                //mLoggedInStatusTextView.setText("Logged in as " + name + " (" + authData.getProvider() + ")");
            }

            final Firebase loginRef = mFBHelper.getLoginRef(authData.getUid());
            final Activity loginActivity = this;

            mFBHelper.SubscribeToModelUpdates(this, FBModelIdentifier.getIdentfier(Login.class, 0, authData), authData.getUid(), true);
            /*loginRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Login login = null;

                    if (dataSnapshot.exists()) {

                        login = dataSnapshot.getValue(Login.class);
                        login.loadLinkedObjects();

                    } else { //First time login with this auth

                        login = new Login(authData.getProvider());
                        loginRef.setValue(login);
                    }

                    if (login != null) {
                        login.FBKey = dataSnapshot.getKey();

                        mFBHelper.set_login(login);
                        mLoginViewSwitcher.setDisplayedChild(mLoginViewSwitcher.indexOfChild(findViewById(R.id.logged_in_view)));

                        if (login.getIsNotProvisioned() == true ||
                                login.getUser().length() != 20) { //Find and associate User
                            mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_ask_memberid)));
                        } else {
                            mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_home)));
                            mFBHelper.set_loggedInUser(login.linkedUser);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    showErrorDialog(firebaseError.getMessage());
                }
            });*/
        } else {

            /* No authenticated user show all the login buttons*/
            mFacebookLoginButton.setVisibility(View.VISIBLE);
            mGoogleLoginButton.setVisibility(View.VISIBLE);
            mTwitterLoginButton.setVisibility(View.VISIBLE);
            mPasswordLoginButton.setVisibility(View.VISIBLE);
            //mLoggedInStatusTextView.setVisibility(View.GONE);
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        invalidateOptionsMenu();
    }

    private void setLoggedInMACUser(Login login, AuthData authData, DataSnapshot dataSnapshot, Firebase loggedInUserRef){

        User linkedUser = login.linkedUser;
        mFBHelper.set_loggedInUser(linkedUser);
    }

    /**
     * Show errors to users
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        if(authData == null) {
            mLoginViewSwitcher.setDisplayedChild(mLoginViewSwitcher.indexOfChild(findViewById(R.id.login_view)));
        }
    }

    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {

        if (identifier.IsIntendedObject(model, Login.class)) {

            Login login = (Login) model;

            if (login != null) {

                mFBHelper.set_login(login);
                mLoginViewSwitcher.setDisplayedChild(mLoginViewSwitcher.indexOfChild(findViewById(R.id.logged_in_view)));

                if (login.getIsNotProvisioned() == true ||
                        login.getUser().length() != 20) { //Find and associate User
                    mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_ask_memberid)));
                } else {
                    mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_home)));
                }
            }

        }
    }

    @Override
    public void onCancel(FBModelIdentifier identifier, FirebaseError error) {

    }

    @Override
    public void onNullData(FBModelIdentifier identifier, String key) {

        if (identifier.getIntendedClass().equals(Login.class)) {
            //First time login with this auth
            AuthData authData = (AuthData)identifier.getPayload();
            Login login = new Login(authData.getProvider());
            login.FBKey = key;
            mFBHelper.set_login(login);
            mFBHelper.setFBModelValue(key, login);
            mFBHelper.addModelListener(key, login, this);

            mLoginViewSwitcher.setDisplayedChild(mLoginViewSwitcher.indexOfChild(findViewById(R.id.logged_in_view)));

            if (login.getIsNotProvisioned() == true ||
                    login.getUser().length() != 20) { //Find and associate User
                mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_ask_memberid)));
            } else {
                mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_home)));
            }
        }
        if(identifier.getIntendedClass().equals(User.class)) {
            showErrorDialog("Invalid Member Number. Please retry with valid Member Number.");
            mMemberId.setText("");
        }
    }

    @Override
    public void onException(Exception x) {
        showErrorDialog(x.getMessage());
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {

        if(modelIdentifier.IsIntendedObject(model, User.class)){
            Login login = mFBHelper.getLogin();
            User user = (User)model;

            login.user = user.FBKey;
            if(login.isNotProvisioned == true) {
                login.isNotProvisioned = false;
                mFBHelper.setFBModelValue(login.FBKey, login);
            }
            login.linkedUser = user;

            if(user.logins == null){
                user.logins = new HashMap<String, Boolean>();
            }
            if(!user.logins.containsKey(login.FBKey)){
                user.logins.put(login.FBKey, true);
                mFBHelper.setFBModelValue(user.FBKey, user);
            }

            mFBHelper.set_loggedInUser(user);
            mFBHelper.getLoginRef(login.FBKey).setValue(login);
            mLoggedinViewSwitcher.setDisplayedChild(mLoggedinViewSwitcher.indexOfChild(findViewById(R.id.logged_in_home)));

            user.loadLinkedObjects();
        }
    }

    @Override
    public void onChildChanged(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key) {

    }

    @Override
    public void onChildRemoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model) {

    }

    @Override
    public void onChildMoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key) {

    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }

    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mAuthProgressDialog.show();
            mFBHelper.getFirebaseRef().authWithOAuthToken("facebook", token.getToken(), new AuthResultHandler("facebook"));
        } else {
            // Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
                mFBHelper.unauth();
                setAuthenticatedUser(null);
            }
        }
    }

    /* ************************************
     *              GOOGLE                *
     **************************************
     */
    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        mAuthProgressDialog.show();
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(LoginActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mGoogleLoginClicked = false;
                if (token != null) {
                    /* Successfully got OAuth token, now login with Google */
                    mFBHelper.getFirebaseRef().authWithOAuthToken("google", token, new AuthResultHandler("google"));
                } else if (errorMessage != null) {
                    mAuthProgressDialog.hide();
                    showErrorDialog(errorMessage);
                }
            }
        };
        task.execute();
    }

    @Override
    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
        getGoogleOAuthTokenAndLogin();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = result;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                if(!result.hasResolution()) {//Show local error dialog
                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
                }
                else {
                    resolveSignInError();
                }
            } else {
                Log.e(TAG, result.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ignore
    }

    /* ************************************
     *               TWITTER              *
     **************************************
     */
    private void loginWithTwitter() {
        startActivityForResult(new Intent(this, TwitterOAuthActivity.class), RC_TWITTER_LOGIN);
    }

    /* ************************************
     *              PASSWORD              *
     **************************************
     */
    @OnClick(R.id.btnLoginWithEmailPassword)
    public void loginWithPassword() {
        mAuthProgressDialog.show();
        mFBHelper.getFirebaseRef().authWithPassword(
                mFBLoginEmail.getText().toString(),
                mFBLoginPassword.getText().toString(),
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        mAuthProgressDialog.hide();
                        setAuthenticatedUser(authData);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        if (firebaseError.getCode() == FirebaseError.USER_DOES_NOT_EXIST) {
                            createLoginPasswordUser();
                        }
                        else if(firebaseError.getCode() == FirebaseError.INVALID_EMAIL ||
                                firebaseError.getCode() == FirebaseError.INVALID_PASSWORD){
                            mAuthProgressDialog.hide();
                            showErrorDialog("Invalid Email/Password combination provided. Please try again with valid credentials.");
                        }
                        else{
                            mAuthProgressDialog.hide();
                            showErrorDialog("Unable to login with Email/Password");
                        }
                    }
                });
    }

    private void createLoginPasswordUser() {
        mFBHelper.getFirebaseRef().createUser(
                mFBLoginEmail.getText().toString(),
                mFBLoginPassword.getText().toString(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        loginWithPassword();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        mAuthProgressDialog.hide();
                        showErrorDialog("Unable to create new user with Email/Password");
                    }
                });
    }

    /* ************************************
     *             ANONYMOUSLY            *
     **************************************
     */
    private void loginAnonymously() {
        mAuthProgressDialog.show();
        mFBHelper.getFirebaseRef().authAnonymously(new AuthResultHandler("anonymous"));
    }
}