package com.mac.themac.fragment;

import android.net.Uri;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.mac.themac.R;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.Session;

import java.util.Date;

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

    private void createReservation(){
        //TODO this all still needs to be linked to the layout and tested
        Session session = new Session();
        session.setDate(date);
        session.setDuration(duration);
        session.setLocation(location);
        Firebase sessionRef = new Firebase(getString(R.string.firebase_url) + "/sessions");
        Firebase newSessionRef = sessionRef.push();
        newSessionRef.setValue(session);
        String sessionKey = newSessionRef.getKey();

        //TODO use logged in user data to fill out empty fields
        Reservation reservation = new Reservation();
        reservation.setDateReserved(new Date());
        reservation.setLocation(location);
//        reservation.setFirstName();
//        reservation.setHasGuest();
        reservation.setInterest(interest);
        reservation.setIsAdvRes(isAdvRes);
//        reservation.setIsJunior();
//        reservation.setLastName();
//        reservation.setMemberNumber();
//        reservation.setName();
//        reservation.setNote();
//        reservation.setReservationUser();
//        reservation.setReservingUser();
        reservation.setSession(sessionKey);
        reservation.setStatus("Reserved");
        reservation.setType("Reservation");
//        reservation.setWantsPartner();
        Firebase reservationRef = new Firebase(getString(R.string.firebase_url) + "/reservations");
        Firebase newReservationRef = reservationRef.push();
        newReservationRef.setValue(reservation);
        String reservationKey = newReservationRef.getKey();
        newSessionRef.child("reservation").setValue(reservationKey);
        Firebase locationRef = new Firebase(getString(R.string.firebase_url) + "/locations/" + location);
        locationRef.child("sessions").push().child(sessionKey).setValue(true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}
