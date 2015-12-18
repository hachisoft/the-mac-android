package com.mac.themac.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.renderscript.RenderScript;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mac.themac.R;
import com.mac.themac.adapter.ReservationsAdapter;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.User;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.ModelCollectionListener;
import com.mac.themac.utility.DownloadImageTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reservations extends FragmentWithTopActionBar{

    @Bind(R.id.lv_reservation)ListView _lvReservations;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Reservations newInstance(String param1, String param2) {
        Reservations fragment = new Reservations();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Reservations() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_reservations;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.my_reservations;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        final User loggedInUser = mFBHelper.getLoggedInUser();
        Collections.sort(loggedInUser.linkedReservations);
        final ReservationsAdapter adapter = new ReservationsAdapter(this.getActivity(), R.layout.reservation_list_row, loggedInUser.linkedReservations);
        _lvReservations.setAdapter(adapter);
        loggedInUser.setCollectionUpdateListner(loggedInUser.linkedReservations, new ModelCollectionListener() {
            @Override
            public void onCollectionUpdated(List<? extends FBModelObject> linkedCollection, FBModelObject fbObject, boolean isAdded) {
                Collections.sort(loggedInUser.linkedReservations);
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
