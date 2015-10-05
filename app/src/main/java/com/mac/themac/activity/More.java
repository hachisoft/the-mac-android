package com.mac.themac.activity;

import android.net.Uri;
import android.widget.ToggleButton;

import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.fragment.Closures;
import com.mac.themac.fragment.Directory;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.fragment.MensBar;
import com.mac.themac.fragment.Parking;

import butterknife.OnClick;

public class More extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener {

    @OnClick(R.id.btnMacInformation)
    public void showMacInfo(){
        TheMACApplication.startActivity(this, MACInformation.class);
    }

    @OnClick(R.id.btnMyAccountGrey)
    public void openMyAccount(){
        launchMyAccount();
    }

    @OnClick(R.id.btnTennisGrey)
    public void openTennis(){
        launchTennisCourts();
    }

    @OnClick(R.id.btnFindGrey)
    public void openFind(){
        launchFind();
    }

    @OnClick(R.id.btnClosures)
    public void showClosures(){
        showFragment(new Closures(), R.id.moreContainer);
    }

    @OnClick(R.id.btnMensBar)
    public void showMensBar(){
        showFragment(new MensBar(), R.id.moreContainer);
    }

    @OnClick(R.id.btnParking)
    public void showParking(){
        showFragment(new Parking(), R.id.moreContainer);
    }

    @OnClick(R.id.btnDirectory)
    public void showDirectory(){
        showFragment(new Directory(), R.id.moreContainer);
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.activity_more;
    }

    @Override
    ToggleButton getActiveButton() {
        return _btnMore;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
