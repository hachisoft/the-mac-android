package com.mac.themac.activity;

import android.net.Uri;
import android.widget.ToggleButton;

import com.mac.themac.R;
import com.mac.themac.fragment.FragmentWithTopActionBar;

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
}
