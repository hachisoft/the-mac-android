package com.mac.themac.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.ParkingProjection;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.utility.FirebaseHelper;
import com.mac.themac.widget.ParkingWidgetDay;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Parking extends FragmentWithTopActionBar implements FBChildListener {
    @Bind(R.id.table_row) TableRow parkingWidgetRow;
    @Bind(R.id.table_layout) TableLayout parkingLayout;
    FirebaseHelper _FBHelper;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Profile.
     */
    public static Parking newInstance() {
        Parking fragment = new Parking();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Parking() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_parking;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.parking;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _FBHelper = TheMACApplication.theApp.getFirebaseHelper();
        _FBHelper.SubscribeToChildUpdates(this, ParkingProjection.class, new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "date"));
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {
        if(modelIdentifier.IsIntendedObject(model, ParkingProjection.class)){
            ParkingProjection pp = (ParkingProjection) model;
            Calendar c = Calendar.getInstance();
            c.setTime(pp.date);
            c.add(Calendar.DATE, 1);
            if( c.after(Calendar.getInstance()) && parkingWidgetRow.getChildCount() <7 ) {
                ParkingWidgetDay widget = new ParkingWidgetDay(getActivity());
                widget.setParkingProjection(pp);
                widget.setGravity(Gravity.CENTER);
                parkingWidgetRow.addView(widget);
                parkingLayout.setStretchAllColumns(true);
            }
        }
    }

    @Override
    public void onChildChanged(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key) {

    }

    @Override
    public void onChildRemoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model) {

    }

    @Override
    public void onChildMoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key) {

    }

    @Override
    public void onCancel(FBModelIdentifier identifier, FirebaseError error) {

    }

    @Override
    public void onNullData(FBModelIdentifier identifier, String key) {

    }

    @Override
    public void onException(Exception x) {

    }
}
