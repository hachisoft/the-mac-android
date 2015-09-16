package com.mac.themac.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.Event;
import com.mac.themac.model.Fee;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 9/15/2015.
 */
public class EventDetails extends FragmentWithTopActionBar implements FBModelListener {
    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_code) TextView tvCode;
    @Bind(R.id.tv_cost) TextView tvCost;
    @Bind(R.id.tv_days) TextView tvDays;
    @Bind(R.id.tv_timeslot) TextView tvTimeSlot;
    @Bind(R.id.tv_dates) TextView tvDates;
    @Bind(R.id.tv_description) TextView tvDescription;

    private static String ARG_EVENT_KEY = "event_key";
    private String eventKey;
    private Event event;

    public static EventDetails newInstance(String eventKey){
        EventDetails frag = new EventDetails();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_KEY, eventKey);
        frag.setArguments(args);
        return frag;
    }

    public EventDetails(){}

    private void toViews(){
        tvTitle.setText(event.getTitle());
        tvCode.setText(event.getNumber());
        tvDescription.setText(event.getDescription());
        tvDays.setText(event.getDaysOfWeek());
        SimpleDateFormat format = new SimpleDateFormat("M/dd/yy");
        tvDates.setText(format.format(event.getStartDate()) + " - " + format.format(event.getEndDate()));
        String cost;
        if(event.linkedFees==null || event.linkedFees.size()==0)
            cost = "";
        else if(event.linkedFees.size()==1)
            cost = "$" + Double.toString(((Fee)event.linkedFees.get(0)).amount);
        else{
            cost = "";
            for(FBModelObject object: event.linkedFees){
                Fee fee = (Fee) object;
                cost += "$" + Double.toString(fee.amount) + " (" + fee.type + "), ";
            }
            cost = cost.substring(0, cost.length()-2);
        }
        tvCost.setText(cost);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
           eventKey = getArguments().getString(ARG_EVENT_KEY);
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_event_details;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FirebaseHelper fbHelper = TheMACApplication.theApp.getFirebaseHelper();
        fbHelper.SubscribeToModelUpdates(this, new FBModelIdentifier(Event.class), eventKey);
    }


    @Override
    protected int getTitleResourceId() {
        return R.string.event_info;
    }

    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
        if(identifier.IsIntendedObject(model, Event.class)) {
            event = (Event) model;
            event.loadLinkedObjects();
            toViews();
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
