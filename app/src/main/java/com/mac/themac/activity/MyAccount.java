package com.mac.themac.activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mac.themac.R;
import com.mac.themac.fragment.AccountStatement;
import com.mac.themac.fragment.CurrentCharges;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.fragment.Interests;
import com.mac.themac.fragment.Profile;
import com.mac.themac.fragment.Reservations;

import butterknife.Bind;
import butterknife.OnClick;

public class MyAccount extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener{

    @Bind(R.id.txtMemberId) TextView _memberId;
    @Bind(R.id.txtMemberSince) TextView _memberSince;
    @Bind(R.id.txtStatus) TextView _memberStatus;

    @OnClick(R.id.btnMyProfile)
    public void showMyProfile(View v){

        Profile profileFragment = new Profile();
        showFragment(profileFragment, R.id.myAccountContainer);
    }

    @OnClick(R.id.btnMyInterests)
    public void showMyInterests(){
        showFragment(new Interests(), R.id.myAccountContainer);
    }

    @OnClick(R.id.btnMyReservations)
    public void showMyReservations(){
        showFragment(new Reservations(), R.id.myAccountContainer);
    }

    @OnClick(R.id.btnCurrentCharges)
    public void showCurrentCharges(){
        showFragment(new CurrentCharges(), R.id.myAccountContainer);
    }

    @OnClick(R.id.btnMyStatement)
    public void showMyStatement(){
        showFragment(new AccountStatement(), R.id.myAccountContainer);
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.activity_my_account;
    }

    @Override
    ToggleButton getActiveButton() {
        return _btnAccount;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
