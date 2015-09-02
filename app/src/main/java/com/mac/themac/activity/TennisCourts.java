package com.mac.themac.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.mac.themac.R;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.model.Interest;
import com.mac.themac.model.Location;
import com.mac.themac.model.ReservationRules;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;

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
    }

    public FirebaseHelper getFBHelper(){
        return _FBHelper;
    }


}
