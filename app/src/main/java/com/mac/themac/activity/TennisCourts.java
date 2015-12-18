package com.mac.themac.activity;

import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.fragment.CourtReservation;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.model.Interest;
import com.mac.themac.model.Location;
import com.mac.themac.model.ReservationRule;
import com.mac.themac.model.Session;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.utility.FirebaseHelper;
import com.mac.themac.widget.CourtReservationTimeBlockWidget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO set up reservation rules checks
public class TennisCourts extends ActivityWithBottomActionBar
                            implements FragmentWithTopActionBar.OnFragmentInteractionListener,
                                        FBChildListener,FBModelListener
{
    private Calendar calendar;
    private long daysToShow = 1;
    private int dayCount = 1;

    private ReservationRule reservationRule;
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

    @Override
    int getLayoutResourceId() {
        return R.layout.activity_tennis_courts;
    }

    @Override
    ToggleButton getActiveButton() {
        return _btnTennis;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getInterest();
        calendar = Calendar.getInstance();
        updateDateLabel();
    }

    public void showCourtReservationFragment(Long date, Long duration, boolean isAdvRes, String location, String interest, String locationName){
        CourtReservation courtReservation = CourtReservation.newInstance(date, duration, isAdvRes, location, interest, locationName);
        showFragment(courtReservation, R.id.holder);
    }

    private void updateDateLabel(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd");
        dateLabel.setText(format.format(calendar.getTime()));
    }

    private void updateCountLabel(){
        countLabel.setText("Day " + Integer.toString(dayCount) + " of " + Long.toString(daysToShow));
    }

    private void getInterest(){

        _FBHelper.SubscribeToChildUpdates(this, Interest.class,
                new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "name",
                        FBQueryIdentifier.Qualifier.equalTo, "Tennis"));

        /*Firebase tennis = TheMACApplication.theApp.getFirebaseHelper().
                getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.interests, true);
        Query queryRef = tennis.orderByChild("name").equalTo("Tennis");
        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    interest = dataSnapshot.getValue(Interest.class);
                    interest.FBKey = dataSnapshot.getKey();
                    getReservationRules(interest.FBKey);
                    getLocations(interest.FBKey);
                }
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
        });*/
    }

    private void constructScheduleForDay(){
        mAdapter = new ReservationAdapter(this, -1);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        long begin;
        long end;
        long duration;
        if(day == Calendar.SATURDAY){
            begin = reservationRule.saturdayPlayBegins;
            end = reservationRule.saturdayPlayEnds;
        } else if(day == Calendar.SUNDAY){
            begin = reservationRule.sundayPlayBegins;
            end = reservationRule.sundayPlayEnds;
        } else {
            begin = reservationRule.weekdayPlayBegins;
            end = reservationRule.weekdayPlayEnds;

        }
        duration = reservationRule.sessionLength;

        for(long i = begin; i < end; i+= duration){
            TimeSlot slot = new TimeSlot();
            slot.startTime = i;
            mAdapter.add(slot);
        }
        for(int i = 0; i < sessions.size(); i++){
            for(Session session: sessions.get(i))
                assignSessionToTimeSlot(session, i);
        }
        widgetList.setDivider(null);
        widgetList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void getReservationRules(String interest){

        _FBHelper.SubscribeToChildUpdates(this, ReservationRule.class,
                new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "interest",
                        FBQueryIdentifier.Qualifier.equalTo, interest));

        /*Firebase rulesRef = _FBHelper.getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.reservationRules, true);
        Query queryRef = rulesRef.orderByChild("interest").equalTo(interest);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    reservationRule = dataSnapshot.getValue(ReservationRule.class);
                    constructScheduleForDay();
                    daysToShow = reservationRule.generalWindowLength;
                    if (calendar.get(Calendar.MINUTE) + (calendar.get(Calendar.HOUR_OF_DAY) * 60) >= reservationRule.timeRegistrationOpens)
                        daysToShow += reservationRule.advancedWindowLength;

                    updateCountLabel();
                }
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
        });*/
    }

    private void getSession(String key, final int pos){

        _FBHelper.SubscribeToModelUpdates(this, new FBModelIdentifier(Session.class, 0, pos), key);

        /*Firebase sessionRef = _FBHelper.getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.sessions, key);
        sessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Session session = dataSnapshot.getValue(Session.class);
                    //TODO determine if Date fits window here?
                    sessions.get(pos).add(session);
                    assignSessionToTimeSlot(session, pos);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/
    }

    private void assignSessionToTimeSlot(Session session, int pos){
        Calendar cal = Calendar.getInstance();
        cal.setTime(session.date);
        if(cal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                cal.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)){
            long mins = cal.get(Calendar.MINUTE) + (cal.get(Calendar.HOUR_OF_DAY) * 60);
            for(int i = 0; i < mAdapter.getCount(); i++){
                TimeSlot slot = mAdapter.getItem(i);
                if(mins >= slot.startTime && mins < slot.startTime + reservationRule.sessionLength){
                    slot.sessions.put(pos, session);
                    if (widgetList != null) {
                        mAdapter.notifyDataSetChanged();
                        widgetList.invalidate();
                    }
                }
            }
        }
    }


    private void getLocations(String interest) {

        _FBHelper.SubscribeToChildUpdates(this, Location.class,
                new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "interest",
                        FBQueryIdentifier.Qualifier.equalTo, interest));
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {

        if(modelIdentifier.IsIntendedObject(model, Location.class)) {
            Location location = (Location)model;
            locations.add(location);
            sessions.add(new ArrayList<Session>());
            if (location.sessions != null) {
                for (String sessionKey : location.sessions.keySet()) {
                    if ( location.sessions.get(sessionKey) == true) {
                        getSession(sessionKey, sessions.size() - 1);
                    }
                }
            }
            if (widgetList != null) {
                mAdapter.notifyDataSetChanged();
                widgetList.invalidate();
            }
        }
        else if(modelIdentifier.IsIntendedObject(model, Interest.class)){
            interest = (Interest)model;
            getReservationRules(interest.FBKey);
            getLocations(interest.FBKey);
        }
        else if(modelIdentifier.IsIntendedObject(model, ReservationRule.class)){
            reservationRule = (ReservationRule)model;
            constructScheduleForDay();
            daysToShow = reservationRule.generalWindowLength;
            if (calendar.get(Calendar.MINUTE) + (calendar.get(Calendar.HOUR_OF_DAY) * 60) >= reservationRule.timeRegistrationOpens)
                daysToShow += reservationRule.advancedWindowLength;

            updateCountLabel();
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
    public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
        if(identifier.IsIntendedObject(model, Session.class)){
            Session session = (Session)model;
            int pos = (int)identifier.getPayload();
            //TODO determine if Date fits window here?
            sessions.get(pos).add(session);
            assignSessionToTimeSlot(session, pos);
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

    private class TimeSlot {
        Long startTime;
        int selected = -1;
        SparseArray<Session> sessions = new SparseArray<>();
        //TODO need to add support for reservations
//        SparseArray<Reservation> reservations;
    }

    private class ReservationAdapter extends ArrayAdapter<TimeSlot> {
        public ReservationAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup container){
            final CourtReservationTimeBlockWidget view;
            if(convertView == null){
                view = new CourtReservationTimeBlockWidget(getContext());
            } else {
                view = (CourtReservationTimeBlockWidget) convertView;
                int lastPos = (int) view.getTag();
                getItem(lastPos).selected = view.getSelected();
            }
            view.setTag(position);
            view.resetButtons();
            view.setTimeLabel(getItem(position).startTime);
            for(int i = 0; i < 9; i ++){
                if(getItem(position).sessions.get(i)!=null){
                    view.setButtonStatus(i, getItem(position).sessions.get(i));
                }
            }
            view.setSelectedButton(getItem(position).selected);
            view.getActionButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (view.getSelectedButtonState()) {
                        case available:
                            calendar.set(Calendar.HOUR_OF_DAY, getItem(position).startTime.intValue()/60);
                            calendar.set(Calendar.MINUTE, getItem(position).startTime.intValue()%60);
                            showCourtReservationFragment(calendar.getTimeInMillis(), reservationRule.sessionLength, dayCount>reservationRule.generalWindowLength, locations.get(view.getSelected()).FBKey, interest.FBKey, locations.get(view.getSelected()).name);
                            break;
                    }

                }
            });
            return view;
        }
    }

    public void addSession(Session session){
        for(int i = 0; i < locations.size(); i ++){
            if(locations.get(i).FBKey.equals(session.location)){
                assignSessionToTimeSlot(session, i);
                break;
            }
        }
    }

    public FirebaseHelper getFBHelper(){
        return _FBHelper;
    }


}
