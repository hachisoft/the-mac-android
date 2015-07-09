package com.mac.themac.activity;

import android.net.Uri;
import android.widget.ToggleButton;

import com.mac.themac.R;
import com.mac.themac.fragment.FragmentWithTopActionBar;

public class Bill extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener {

    @Override
    int getLayoutResourceId() {
        return R.layout.activity_bill;
    }

    @Override
    ToggleButton getActiveButton() {
        return _btnBill;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
