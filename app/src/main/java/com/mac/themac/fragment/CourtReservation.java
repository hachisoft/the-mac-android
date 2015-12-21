package com.mac.themac.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.activity.TennisCourts;
import com.mac.themac.model.Closure;
import com.mac.themac.model.Location;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.Session;
import com.mac.themac.model.User;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.FirebaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 9/9/2015.
 */
public class CourtReservation extends FragmentWithTopActionBar implements FBModelListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = "date";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_INTEREST = "interest";
    private static final String ARG_ADVANCED_RES = "isAdvRes";
    private static final String ARG_LOCATION_NAME = "locName";

    private Date date;
    private long duration;
    private boolean isAdvRes;
    private String location;
    private String interest;
    private String locationName;
    private Session session;

    @Bind(R.id.tvCourt) TextView court;
    @Bind(R.id.tvDate) TextView tvDate;
    @Bind(R.id.tvTime) TextView time;
    @Bind(R.id.editNotes) EditText notes;
    @Bind(R.id.cbBallMachine) CheckBox ballMachine;
    @Bind(R.id.cancel) Button cancel;

    public static CourtReservation newInstance(Long date, Long duration, boolean isAdvRes, String location, String interest, String locationName) {
        CourtReservation fragment = new CourtReservation();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date);
        args.putLong(ARG_DURATION, duration);
        args.putBoolean(ARG_ADVANCED_RES, isAdvRes);
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_INTEREST, interest);
        args.putString(ARG_LOCATION_NAME, locationName);
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
        court.setText(locationName);
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd");
        tvDate.setText(format.format(date));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long mins = cal.get(Calendar.HOUR_OF_DAY)*60 + cal.get(Calendar.MINUTE);
        time.setText((Long.toString(mins / 60 % 12 == 0 ? 12 : mins / 60 % 12) + ":" + String.format("%02d", mins % 60) + " " + (mins / 60 >= 12 ? "PM" : "AM")));
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
            locationName = getArguments().getString(ARG_LOCATION_NAME);
        }
    }

    @OnClick(R.id.cancel)
    public void cancel(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.reserve)
    public void createReservation(){
        //TODO this all still needs to be linked to the layout and tested
        session = new Session();
        session.date = date;
        session.duration = duration;
        session.location = location;
        Firebase sessionRef = TheMACApplication.theApp.getFirebaseHelper().getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.sessions);
        Firebase newSessionRef = sessionRef.push();
        newSessionRef.setValue(session);
        String sessionKey = newSessionRef.getKey();
        session.FBKey = sessionKey;

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
        reservation.reservationUser = user.FBKey;
        reservation.reservingUser = user.FBKey;
        reservation.session = sessionKey;
        reservation.status = "Reserved";
        reservation.type = "Reservation";
        reservation.wantsPartner = false;
        Firebase reservationRef = TheMACApplication.theApp.getFirebaseHelper().getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.reservations);
        Firebase newReservationRef = reservationRef.push();
        newReservationRef.setValue(reservation);
        String reservationKey = newReservationRef.getKey();
        newSessionRef.child("reservation").setValue(reservationKey);
        session.reservation = reservationKey;
        FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
        fbHelper.SubscribeToModelUpdates(this, new FBModelIdentifier(Location.class), location);
        //TODO check on ball machine with caleb
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
        if(identifier.IsIntendedObject(model, Location.class)) {
            Location loc = (Location) model;
            if(loc.sessions==null)
                loc.sessions = new HashMap<>();
            loc.sessions.put(session.FBKey, true);
            Firebase locationRef = TheMACApplication.theApp.getFirebaseHelper().getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.locations).child(location);
            locationRef.setValue(loc);
            ((TennisCourts)getActivity()).addSession(session);
            ((TennisCourts)getActivity()).onBackPressed();
        }
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
