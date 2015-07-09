package com.mac.themac.activity;

import android.net.Uri;
import android.widget.ToggleButton;

import com.mac.themac.R;
import com.mac.themac.fragment.FragmentWithTopActionBar;

public class FindEvents extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener {

    @Override
    int getLayoutResourceId() {
        return R.layout.activity_events;
    }

    @Override
    ToggleButton getActiveButton() {
        return _btnFind;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
