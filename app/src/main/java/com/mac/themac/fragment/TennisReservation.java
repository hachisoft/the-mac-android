package com.mac.themac.fragment;

import android.net.Uri;
import android.os.Bundle;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.mac.themac.R;
import com.mac.themac.activity.TennisCourts;
import com.mac.themac.model.Interest;
import com.mac.themac.model.Location;
import com.mac.themac.model.ReservationRules;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;

/**
 * Created by Bryan on 9/1/2015.
 */
public class TennisReservation extends FragmentWithTopActionBar {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseHelper _FBHelper;
    private ReservationRules reservationRules;
    private Interest interest;
    private ArrayList<Location> locations;

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

    public TennisReservation() {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _FBHelper = ((TennisCourts)getActivity()).getFBHelper();
        getInterest();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getInterest(){
        Firebase tennis = _FBHelper.getFirebaseRef().child("interests");
        Query queryRef = tennis.orderByChild("name").equalTo("Tennis");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                interest = new Interest(dataSnapshot);
                interest.loadServerData(_FBHelper.getFirebaseRef(), dataSnapshot);
                getReservationRules(interest.key);
                getLocations(interest.key);
                //TODO use interest to look up any existing reservations
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        queryRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }

    private void getReservationRules(String interest){
        Firebase rulesRef = _FBHelper.getFirebaseRef().child("reservationRules");
        Query queryRef = rulesRef.orderByChild("interest").equalTo(interest);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservationRules = new ReservationRules(dataSnapshot);
                reservationRules.loadServerData(_FBHelper.getFirebaseRef(), dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getLocations(String interest){
        Firebase locationsRef = _FBHelper.getFirebaseRef().child("locations");
        Query queryRef = locationsRef.orderByChild("nsCourseLocationId");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(locations == null)
                    locations = new ArrayList<>();
                Location location = new Location(dataSnapshot);
                location.loadServerData(_FBHelper.getFirebaseRef(), dataSnapshot);
                locations.add(location);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
