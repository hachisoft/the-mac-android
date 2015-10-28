package com.mac.themac.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ToggleButton;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.model.User;
import com.mac.themac.utility.FirebaseHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Samir on 7/8/2015.
 */
public abstract class ActivityWithBottomActionBar extends AppCompatActivity implements Firebase.AuthStateListener {

    abstract int getLayoutResourceId();
    abstract ToggleButton getActiveButton();

    public enum TransitionEffect{
        Slide_from_right, Slide_from_top
    }

    /* A reference to the Firebase */
    protected FirebaseHelper _FBHelper;

    @Bind(R.id.btnMyAccount) ToggleButton _btnAccount;
    @Bind(R.id.btnBill) ToggleButton _btnBill;
    @Bind(R.id.btnTennisCourts) ToggleButton _btnTennis;
    @Bind(R.id.btnFind) ToggleButton _btnFind;
    @Bind(R.id.btnMore) ToggleButton _btnMore;

    @OnClick(R.id.btnMore)
    public void launchMore(){
        TheMACApplication.startActivity(this, More.class);
    }

    @OnClick(R.id.btnMyAccount)
    public void launchMyAccount(){
        if(getActiveButton() != _btnAccount)
            TheMACApplication.startActivity(this, MyAccount.class);
        else
            _btnAccount.setChecked(true);
    }

    @OnClick(R.id.btnBill)
    public void launchBill(){
        if(getActiveButton() != _btnBill)
            TheMACApplication.startActivity(this, Bill.class);
        else
            _btnBill.setChecked(true);
    }

    @OnClick(R.id.btnFind)
    public void launchFind(){
        if(getActiveButton() != _btnFind)
            TheMACApplication.startActivity(this, FindEvents.class);
        else
            _btnFind.setChecked(true);
    }

    @OnClick(R.id.btnTennisCourts)
    public void launchTennisCourts(){
        if(getActiveButton() != _btnTennis)
            TheMACApplication.startActivity(this, TennisCourts.class);
        else
            _btnTennis.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        ButterKnife.bind(this);

        _FBHelper = TheMACApplication.theApp.getFirebaseHelper();

        getActiveButton().setChecked(true);
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
    public void onAuthStateChanged(AuthData authData) {
        if(authData == null) {
            TheMACApplication.theApp.getFirebaseHelper().removeAuthStateListener(this);
            //something triggered authentication to be reset
            //Take back to login screen
            TheMACApplication.startActivity(this, LoginActivity.class);
        }
    }

    public void hideTopActionBar() {
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    public void showTopActionBar() {
        if(getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.app_home);
            getSupportActionBar().show();
        }
    }

    public void showFragment(FragmentWithTopActionBar fragment, int replacingResourceId){
        showFragment(fragment, replacingResourceId, TransitionEffect.Slide_from_top);
    }

    public void showFragment(FragmentWithTopActionBar fragment, int replacingResourceId,
                             TransitionEffect effect){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (effect){
            case Slide_from_right:
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right);
                break;
            case Slide_from_top:
                fragmentTransaction.setCustomAnimations(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom,
                        R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
                default:
        }

        fragmentTransaction.add(replacingResourceId, fragment);
        fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
