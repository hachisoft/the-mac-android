package com.mac.themac.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.fragment.EventDetails;
import com.mac.themac.fragment.EventList;
import com.mac.themac.fragment.EventTypeSelect;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.model.Session;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindEvents extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener,
        FBChildListener{

    @Bind(R.id.tv_event_types) TextView tvEventTypes;
    @Bind(R.id.tv_start_time) TextView tvStartTime;
    @Bind(R.id.tv_end_time) TextView tvEndTime;
    @Bind(R.id.tv_from_date) TextView tvFromDate;
    @Bind(R.id.tv_to_date) TextView tvToDate;
    @Bind(R.id.search_text) TextView tvSearchText;
    private ArrayList<Session> sessions = new ArrayList<>();
    private Calendar fromDate;
    private Calendar toDate;
    private ArrayList<String> selectedEventTypes = new ArrayList<>();
    private EventList listFragment;
    private long startTime = 345;
    private long endTime = 1380;

    //TODO build date picker
    //TODO build time picker
    //TODO get selected event types back from fragment

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        fromDate = Calendar.getInstance();
        fromDate.set(Calendar.HOUR_OF_DAY, 5);
        fromDate.set(Calendar.MINUTE, 30);
        toDate = Calendar.getInstance();
        toDate.add(Calendar.DAY_OF_YEAR, 6);
        ButterKnife.bind(this);
        tvEventTypes.setText("All");
        tvStartTime.setText("5:45 AM");
        tvEndTime.setText("11:00 PM");
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        tvFromDate.setText(format.format(fromDate.getTime()));
        tvToDate.setText(format.format(toDate.getTime()));
        _FBHelper.SubscribeToChildUpdates(this, Session.class,
                new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "date"));
    }

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

    @OnClick(R.id.search)
    public void onSearch(){
        listFragment = EventList.newInstance(fromDate.getTime(), toDate.getTime());
        showFragment(listFragment, R.id.container);
    }

    @OnClick(R.id.tv_event_types)
    public void selectEventTypes(){
        showFragment(EventTypeSelect.newInstance(selectedEventTypes), R.id.container);
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {
        if(modelIdentifier.IsIntendedObject(model, Session.class)){
            Session session = (Session) model;
            if(session.getEvent()==null)
                return;
            if(session.getDate().before(new Date(fromDate.getTimeInMillis())))
                return;
            session.loadLinkedObjects();
            sessions.add(session);
        }
    }

    public void loadSessionsForDate(Calendar date){
        Calendar temp = Calendar.getInstance();
        for(Session session: sessions){
            temp.setTime(session.getDate());
            if(temp.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)){
                //TODO assess other filters here
                boolean typesTest = true;
                boolean withinTimeWindow = true;
                boolean nameMatch = true;
                if(selectedEventTypes.size()>0){
                    typesTest = selectedEventTypes.contains(session.linkedEvent.getInterest());
                }
                long minutes = temp.get(Calendar.HOUR_OF_DAY) * 60 + temp.get(Calendar.MINUTE);
                withinTimeWindow = minutes >= startTime && minutes <= endTime;
                String search = tvSearchText.getText().toString();
                if(!search.isEmpty()){
                    nameMatch = session.linkedEvent.getTitle().toLowerCase().contains(search.toLowerCase());
                }
                if(typesTest && withinTimeWindow && nameMatch)
                    listFragment.addSession(session);
            }
        }
    }

    public void showEventDetails(String event){
        EventDetails frag = EventDetails.newInstance(event);
        showFragment(frag, R.id.container);
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
