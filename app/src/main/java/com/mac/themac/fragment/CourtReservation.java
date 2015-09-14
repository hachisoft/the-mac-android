package com.mac.themac.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.mac.themac.R;
import com.mac.themac.activity.TennisCourts;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.Session;
import com.mac.themac.model.User;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 9/9/2015.
 */
public class CourtReservation extends FragmentWithTopActionBar{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = "date";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_INTEREST = "interest";
    private static final String ARG_ADVANCED_RES = "isAdvRes";

    private Date date;
    private long duration;
    private boolean isAdvRes;
    private String location;
    private String interest;

    @Bind(R.id.editNotes) EditText notes;
    @Bind(R.id.cbBallMachine) CheckBox ballMachine;
    @Bind(R.id.cancel) Button cancel;

    public static CourtReservation newInstance(Long date, Long duration, boolean isAdvRes, String location, String interest) {
        CourtReservation fragment = new CourtReservation();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date);
        args.putLong(ARG_DURATION, duration);
        args.putBoolean(ARG_ADVANCED_RES, isAdvRes);
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_INTEREST, interest);
        fragment.setArguments(args);
        return fragment;
    }

    public CourtReservation() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_reserve_court;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.my_reservations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = new Date(getArguments().getLong(ARG_DATE));
            duration = getArguments().getLong(ARG_DURATION);
            isAdvRes = getArguments().getBoolean(ARG_ADVANCED_RES);
            location = getArguments().getString(ARG_LOCATION);
            interest = getArguments().getString(ARG_INTEREST);
        }
    }

    @OnClick(R.id.cancel)
    public void cancel(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.reserve)
    public void createReservation(){
        //TODO this all still needs to be linked to the layout and tested
        Session session = new Session();
        session.setDate(date);
        session.setDuration(duration);
        session.setLocation(location);
        Firebase sessionRef = new Firebase(getString(R.string.firebase_url) + "/sessions");
        Firebase newSessionRef = sessionRef.push();
        newSessionRef.setValue(session);
        String sessionKey = newSessionRef.getKey();

        User user = ((TennisCourts)getActivity()).getFBHelper().getLoggedInUser();
        Reservation reservation = new Reservation();
        reservation.dateReserved = new Date();
        reservation.location = location;
        reservation.firstName = user.firstName;
        reservation.hasGuest = false;
        reservation.interest = interest;
        reservation.isAdvRes = isAdvRes;
        reservation.isJunior = user.isJunior;
        reservation.lastName = user.lastName;
        reservation.memberNumber = user.memberNumber;
        reservation.name = user.firstName + " " + user.lastName;
        reservation.note = notes.getText().toString();
        //TODO check with Caleb on this
        reservation.reservationUser = user.FBKey;
        reservation.reservingUser = user.FBKey;
        reservation.session = sessionKey;
        reservation.status = "Reserved";
        reservation.type = "Reservation";
        reservation.wantsPartner = false;
        Firebase reservationRef = new Firebase(getString(R.string.firebase_url) + "/reservations");
        Firebase newReservationRef = reservationRef.push();
        newReservationRef.setValue(reservation);
        String reservationKey = newReservationRef.getKey();
        newSessionRef.child("reservation").setValue(reservationKey);
        Firebase locationRef = new Firebase(getString(R.string.firebase_url) + "/locations/" + location);
        locationRef.child("sessions").push().child(sessionKey).setValue(true);
        //TODO check on ball machine with caleb
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
