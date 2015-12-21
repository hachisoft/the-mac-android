package com.mac.themac.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.fragment.DatePickerFragment;
import com.mac.themac.fragment.DatePickerFragment.DatePickerFragmentInterface;
import com.mac.themac.fragment.EventDetails;
import com.mac.themac.fragment.EventList;
import com.mac.themac.fragment.EventSurvey;
import com.mac.themac.fragment.EventTypeSelect;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.fragment.NewGuest;
import com.mac.themac.fragment.ReviewRegistration;
import com.mac.themac.fragment.SelectEventGuests;
import com.mac.themac.fragment.TimePickerFragment;
import com.mac.themac.model.Event;
import com.mac.themac.model.Guest;
import com.mac.themac.model.Session;
import com.mac.themac.model.Survey;
import com.mac.themac.model.SurveyResponse;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBQueryIdentifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindEvents extends ActivityWithBottomActionBar implements FragmentWithTopActionBar.OnFragmentInteractionListener,
        FBChildListener, DatePickerFragmentInterface, TimePickerFragment.TimePickerFragmentInterface {

    private static final int FROM_DATE = 111;
    private static final int TO_DATE = 112;
    private static final int START_TIME = 113;
    private static final int END_TIME = 114;

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
        ((TheMACApplication)getApplication()).setTimeLabel(tvStartTime, startTime);
        ((TheMACApplication)getApplication()).setTimeLabel(tvEndTime, endTime);
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

    @OnClick(R.id.tv_from_date)
    public void selectFromDate(){
        DatePickerFragment.newInstance(FROM_DATE, fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DAY_OF_MONTH)).show(getSupportFragmentManager(), "DatePicker");
    }

    @OnClick(R.id.tv_to_date)
    public void selectToDATE(){
        DatePickerFragment.newInstance(TO_DATE, toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH), toDate.get(Calendar.DAY_OF_MONTH)).show(getSupportFragmentManager(), "DatePicker");
    }

    @OnClick(R.id.tv_start_time)
    public void setStartTime(){
        TimePickerFragment.newInstance(START_TIME, startTime).show(getSupportFragmentManager(), "TimePicker");
    }

    @OnClick(R.id.tv_end_time)
    public void setEndTime(){
        TimePickerFragment.newInstance(END_TIME, endTime).show(getSupportFragmentManager(), "TimePicker");
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {
        if(modelIdentifier.IsIntendedObject(model, Session.class)){
            Session session = (Session) model;
            if(session.event ==null)
                return;
            Date temp = new Date(fromDate.getTimeInMillis());
            if(session.date.before(temp))
                return;
            session.loadLinkedObjects();
            sessions.add(session);
        }
    }

    public void loadSessionsForDate(Calendar date){
        Calendar temp = Calendar.getInstance();
        for(Session session: sessions){
            temp.setTime(session.date);
            if(temp.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR) && temp.get(Calendar.YEAR) == date.get(Calendar.YEAR)){
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
                    try {
                        nameMatch = session.linkedEvent.getTitle().toLowerCase().contains(search.toLowerCase());
                    } catch (Exception e){
                        nameMatch = false;
                    }
                }
                if(typesTest && withinTimeWindow && nameMatch)
                    listFragment.addSession(session);
            }
        }
    }

    public ArrayList<Guest> guests = new ArrayList<>();
    public SparseArray<SurveyResponse> membersSurveys = new SparseArray<>();

    public void saveSurveyResponse(SurveyResponse sr, boolean isMember, int pos){
        if(isMember)
            membersSurveys.put(pos, sr);
        else
            guests.get(pos).surveyResponse = sr;
    }

    private Event event;

    public void setCurrentEvent(Event event){
        this.event = event;
    }

    public void showEventDetails(String event){
        EventDetails frag = EventDetails.newInstance(event);
        showFragment(frag, R.id.container);
    }

    public Event getCurrentEvent(){
        return event;
    }

    private Survey survey;

    public void setCurrentSurvey(Survey survey){
        this.survey = survey;
    }

    public Survey getCurrentSurvey(){
        return survey;
    }

    public void showNewGuest(){
        NewGuest frag = new NewGuest();
        showFragment(frag, R.id.container);
    }

    public void addGuest(Guest guest){
        guests.add(guest);
    }

    public void showSelectEventGuests(){
        guests.clear();
        SelectEventGuests frag = new SelectEventGuests();
        showFragment(frag, R.id.container);
    }

    public void showEventSurvey(boolean isMember, int pos){
        if(event.getSurvey() != null) {
            EventSurvey frag = EventSurvey.newInstance(event.getSurvey(), isMember, pos);
            showFragment(frag, R.id.container);
        }
    }

    public void showReviewRegistration(ArrayList<String> members, ArrayList<Integer> indices){
        ReviewRegistration frag = ReviewRegistration.newInstance(members, indices);
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

    @Override
    public void onDateSet(int dateType, int year, int month, int day) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        if(dateType == FROM_DATE){
            fromDate.set(Calendar.YEAR, year);
            fromDate.set(Calendar.MONTH, month);
            fromDate.set(Calendar.DAY_OF_MONTH, day);
            tvFromDate.setText(format.format(fromDate.getTime()));

        } else if(dateType == TO_DATE){
            toDate.set(Calendar.YEAR, year);
            toDate.set(Calendar.MONTH, month);
            toDate.set(Calendar.DAY_OF_MONTH, day);
            tvToDate.setText(format.format(toDate.getTime()));
        }
    }

    @Override
    public void onTimeSet(int timeType, int hourOfDay, int minute) {
        if(timeType == START_TIME) {
            startTime = hourOfDay * 60 + minute;
            ((TheMACApplication) getApplication()).setTimeLabel(tvStartTime, startTime);
        } else {
            endTime = hourOfDay * 60 + minute;
            ((TheMACApplication) getApplication()).setTimeLabel(tvEndTime, endTime);
        }
    }
}
