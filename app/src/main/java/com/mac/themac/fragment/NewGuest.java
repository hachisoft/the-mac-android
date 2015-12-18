package com.mac.themac.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mac.themac.R;
import com.mac.themac.model.Guest;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 12/17/2015.
 */
public class NewGuest extends FragmentWithTopActionBar {
    @Bind(R.id.et_first_name) EditText etFirstName;
    @Bind(R.id.et_last_name) EditText etLastName;
    @Bind(R.id.cb_is_child) CheckBox cbIsChild;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_new_guest;
    }

    @Override
    protected int getTitleResourceId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    public Guest fromViews(){
        Guest guest = new Guest();
        guest.first = etFirstName.getText().toString();
        guest.last = etLastName.getText().toString();
        guest.isChild = cbIsChild.isChecked();
        return guest;
    }
}
