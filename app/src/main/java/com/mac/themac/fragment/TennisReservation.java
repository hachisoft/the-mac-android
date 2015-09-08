package com.mac.themac.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.mac.themac.model.Session;
import com.mac.themac.utility.FirebaseHelper;
import com.mac.themac.widget.CourtReservationTimeBlockWidget;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 9/1/2015.
 */
public class TennisReservation extends FragmentWithTopActionBar {
    private Calendar calendar;
    private long daysToShow = 1;
    private int dayCount = 1;

    private FirebaseHelper _FBHelper;
    private ReservationRules reservationRules;
    private Interest interest;
    private ArrayList<Location> locations = new ArrayList<>();
    private ReservationAdapter mAdapter;
    private ArrayList<ArrayList<Session>> sessions = new ArrayList<>();

    @Bind(R.id.date_label) TextView dateLabel;
    @Bind(R.id.count_label) TextView countLabel;
    @Bind(R.id.list_view) ListView widgetList;

    @OnClick(R.id.prev)
    public void backOneDay(){
        if(dayCount > 1) {
            changeCalendarDay(-1);
        }
    }

    @OnClick(R.id.next)
    public void forwardOneDay(){
        if(dayCount != daysToShow) {
            changeCalendarDay(1);
        }
    }

    private void changeCalendarDay(int count){
        calendar.add(Calendar.DATE, count);
        constructScheduleForDay();
        dayCount += count;
        updateCountLabel();
        updateDateLabel();
    }

    // TODO: Rename and change types and number of parameters
    public static TennisReservation newInstance(String param1, String param2) {
        TennisReservation fragment = new TennisReservation();
        Bundle args = new Bundle();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        calendar = Calendar.getInstance();
        updateDateLabel();
        return view;
    }

    private void updateDateLabel(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd");
        dateLabel.setText(format.format(calendar.getTime()));
    }

    private void updateCountLabel(){
        countLabel.setText("Day " + Integer.toString(dayCount) + " of " + Long.toString(daysToShow));
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
        Firebase tennis = new Firebase(getString(R.string.firebase_url) + "/interests");
        Query queryRef = tennis.orderByChild("name").equalTo("Tennis");
        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                interest = new Interest(dataSnapshot);
                interest.loadServerData(_FBHelper.getFirebaseRef(), dataSnapshot);
                getReservationRules(interest.key);
                getLocations(interest.key);
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

    private void constructScheduleForDay(){
        mAdapter = new ReservationAdapter(getActivity(), -1);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        long begin;
        long end;
        long duration;
        if(day == Calendar.SATURDAY){
            begin = reservationRules.getSaturdayPlayBegins();
            end = reservationRules.getSaturdayPlayEnds();
        } else if(day == Calendar.SUNDAY){
            begin = reservationRules.getSundayPlayBegins();
            end = reservationRules.getSundayPlayEnds();
        } else {
            begin = reservationRules.getWeekdayPlayBegins();
            end = reservationRules.getWeekdayPlayEnds();

        }
        duration = reservationRules.getSessionLength();

        for(long i = begin; i < end; i+= duration){
            TimeSlot slot = new TimeSlot();
            slot.startTime = i;
            mAdapter.add(slot);
        }
        widgetList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void getReservationRules(String interest){
        Firebase rulesRef = new Firebase(getString(R.string.firebase_url) + "/reservationRules");
        Query queryRef = rulesRef.orderByChild("interest").equalTo(interest);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                reservationRules = dataSnapshot.getValue(ReservationRules.class);
                constructScheduleForDay();
                daysToShow = reservationRules.getGeneralWindowLength();
                if (calendar.get(Calendar.MINUTE) + (calendar.get(Calendar.HOUR_OF_DAY) * 60) >= reservationRules.getTimeRegistrationOpens())
                    daysToShow += reservationRules.getAdvancedWindowLength();
                updateCountLabel();
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

    private void getSession(String key, final int pos){
        Firebase sessionRef = new Firebase(getString(R.string.firebase_url) + "/locations/" + key);
        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Session session = dataSnapshot.getValue(Session.class);
                    //TODO determine if Date fits window here?
                    sessions.get(pos).add(session);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void getLocations(String interest){
        Firebase locationsRef = new Firebase(getString(R.string.firebase_url) + "/locations");
        Query queryRef = locationsRef.orderByChild("interest").equalTo(interest);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Location location = dataSnapshot.getValue(Location.class);
                locations.add(location);
                sessions.add(new ArrayList<Session>());
                DataSnapshot session = dataSnapshot.child("sessions");
                for(DataSnapshot ds: session.getChildren()){
                    if(ds.getValue()==true){
                        getSession(ds.getKey(), sessions.size()-1);
                    }
                }
                mAdapter.notifyDataSetChanged();
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

    private class TimeSlot {
        Long startTime;
        int selected = -1;
        //TODO need to add support for reservations
//        SparseArray<Reservation> reservations;
    }

    private class ReservationAdapter extends ArrayAdapter<TimeSlot> {
        public ReservationAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container){
            CourtReservationTimeBlockWidget view;
            if(convertView == null){
                view = new CourtReservationTimeBlockWidget(getContext());
            } else {
                view = (CourtReservationTimeBlockWidget) convertView;
                int lastPos = (int) view.getTag();
                getItem(lastPos).selected = view.getSelected();
            }
            view.setTag(position);
            view.setButton(getItem(position).selected);
            view.setTimeLabel(getItem(position).startTime);


            return view;
        }
    }
}
