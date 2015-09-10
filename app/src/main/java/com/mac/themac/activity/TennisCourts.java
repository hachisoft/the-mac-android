package com.mac.themac.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.mac.themac.R;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.fragment.TennisReservation;
import com.mac.themac.utility.FirebaseHelper;

public class TennisCourts extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener {


    @Override
    int getLayoutResourceId() {
        return R.layout.activity_tennis_courts;
    }

    @Override
    ToggleButton getActiveButton() {
        return _btnTennis;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        showFragment(TennisReservation.newInstance("",""), R.id.fragment_holder);
    }

    public FirebaseHelper getFBHelper(){
        return _FBHelper;
    }


}
