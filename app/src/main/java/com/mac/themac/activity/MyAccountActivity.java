package com.mac.themac.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.User;
import com.mac.themac.utility.FirebaseHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountActivity extends Activity implements Firebase.AuthStateListener{

    /* A reference to the Firebase */
    private FirebaseHelper _FBHelper;

    @Bind(R.id.btnMyAccount)
    ImageButton _myAccount;

    @OnClick({R.id.btnMore, R.id.txtMore})
    public void launchMore(){
        TheMACApplication.startActivity(this, MoreActivity.class);
    }

    @OnClick({R.id.btnMyAccount, R.id.txtMyAccount})
    public void launchMyAccount(){
        _myAccount.setPressed(true);
    }

    @OnClick({R.id.btnBill, R.id.txtBill})
    public void launchBill(){
        TheMACApplication.startActivity(this, BillActivity.class);
    }

    @OnClick({R.id.btnFind, R.id.txtFind})
    public void launchFind(){
        TheMACApplication.startActivity(this, FindEventsActivity.class);
    }

    @OnClick({R.id.btnTennisCourts, R.id.txtTennisCourts})
    public void launchTennisCourts(){
        TheMACApplication.startActivity(this, TennisCourtsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);

        _FBHelper = TheMACApplication.theApp.getFirebaseHelper();

        User loggedInUser = _FBHelper.getLoggedInUser();
        _myAccount.setPressed(true);
    }

    @OnClick(R.id.btnMore)
    public void logOut(View view){
        TheMACApplication.theApp.getFirebaseHelper().unauth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TheMACApplication.theApp.getFirebaseHelper().addAuthStateListener(this);
    }

    @Override
    protected void onPause() {
        TheMACApplication.theApp.getFirebaseHelper().removeAuthStateListener(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        if(authData == null) {
            TheMACApplication.theApp.getFirebaseHelper().removeAuthStateListener(this);
            //something triggered authentication to be reset
            //Take back to login screen
            TheMACApplication.startActivity(this, LoginActivity.class);
        }
    }
}
