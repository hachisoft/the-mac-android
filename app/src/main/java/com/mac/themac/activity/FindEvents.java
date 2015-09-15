package com.mac.themac.activity;

import android.net.Uri;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mac.themac.R;
import com.mac.themac.fragment.EventTypeSelect;
import com.mac.themac.fragment.FragmentWithTopActionBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class FindEvents extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener {

    @Bind(R.id.tv_event_types) TextView tvEventTypes;

    private ArrayList<String> selectedEventTypes = new ArrayList<>();

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

    @OnClick(R.id.tv_event_types)
    public void selectEventTypes(){
        showFragment(EventTypeSelect.newInstance(selectedEventTypes), R.id.container);
    }
}
