package com.mac.themac.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mac.themac.R;
import com.mac.themac.model.Closure;
import com.mac.themac.model.Event;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.Session;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 9/7/2015.
 */
public class CourtReservationTimeBlockWidget extends LinearLayout {
    @Bind(R.id.time_label) TextView timeLabel;
    @Bind(R.id.detail_label) TextView detailLabel;
    @Bind(R.id.action_button) Button actionButton;
    @Bind(R.id.button_container) LinearLayout buttonContainer;
    @Bind(R.id.court1) CourtButton court1;
    @Bind(R.id.court2) CourtButton court2;
    @Bind(R.id.court3) CourtButton court3;
    @Bind(R.id.court4) CourtButton court4;
    @Bind(R.id.court5) CourtButton court5;
    @Bind(R.id.court6) CourtButton court6;
    @Bind(R.id.court7) CourtButton court7;
    @Bind(R.id.court8) CourtButton court8;
    @Bind(R.id.court9) CourtButton court9;

    private int selected = -1;

    @OnClick({R.id.court1, R.id.court2, R.id.court3, R.id.court4, R.id.court5, R.id.court6, R.id.court7, R.id.court8, R.id.court9})
    public void doClick(CourtButton court){
        for(int i = 0; i < courtButtons.size(); i++){
            courtButtons.get(i).setGreyed(courtButtons.get(i).getCourtNum()!=court.getCourtNum());
            if(courtButtons.get(i).getCourtNum()==court.getCourtNum()) {
                selected = i;
                if(courtButtons.get(i).getCourtState()== CourtButton.CourtState.available) {
                    actionButton.setVisibility(View.VISIBLE);
                    detailLabel.setVisibility(View.GONE);
                } else {
                    actionButton.setVisibility(View.INVISIBLE);
                    Session session = (Session) courtButtons.get(i).getTag();
                    if(session.getReservation()!=null){
                        getReservationForLabel(session.getReservation());
                    } else if(session.getClosure()!=null){
                        getClosureForLabel(session.getClosure());
                    } else if(session.getEvent()!=null){
                        getEventForLabel(session.getEvent());
                    }
                }
            }
        }
    }

    private void getReservationForLabel(String id){
        Firebase fire = new Firebase(getContext().getString(R.string.firebase_url) + "/reservations/" + id);
        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);
                    if(reservation.getName()!=null){
                        detailLabel.setText(reservation.getName());
                        detailLabel.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getClosureForLabel(String id){
        Firebase fire = new Firebase(getContext().getString(R.string.firebase_url) + "/closures/" + id);
        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Closure closure = dataSnapshot.getValue(Closure.class);
                    if(closure.getTitle()!=null){
                        detailLabel.setText(closure.getTitle());
                        detailLabel.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void getEventForLabel(String id){
        Firebase fire = new Firebase(getContext().getString(R.string.firebase_url) + "/events/" + id);
        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Event event = dataSnapshot.getValue(Event.class);
                    if(event.getTitle()!=null){
                        detailLabel.setText(event.getTitle());
                        detailLabel.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private ArrayList<CourtButton> courtButtons = new ArrayList<>();

    public CourtReservationTimeBlockWidget(Context context) {
        super(context);
        init();
    }

    public CourtReservationTimeBlockWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CourtReservationTimeBlockWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.court_widget, this);
        ButterKnife.bind(this);
        courtButtons.add(court1);
        courtButtons.add(court2);
        courtButtons.add(court3);
        courtButtons.add(court4);
        courtButtons.add(court5);
        courtButtons.add(court6);
        courtButtons.add(court7);
        courtButtons.add(court8);
        courtButtons.add(court9);
    }

    public void setTimeLabel(Long time){
        timeLabel.setText(Long.toString(time / 60 % 12 == 0 ? 12 : time / 60 % 12) + ":" + String.format("%02d", time % 60) + " " + (time / 60 >= 12 ? "PM" : "AM"));
    }

    public void resetButtons(){
        selected = -1;
        for(CourtButton cb: courtButtons){
            cb.setCourtState(CourtButton.CourtState.available);
            cb.setTag(null);
            cb.setGreyed(false);
        }
        detailLabel.setVisibility(View.GONE);
        actionButton.setVisibility(View.INVISIBLE);
    }

    public void setSelectedButton(int selected){
        this.selected = selected;
        if(selected != -1)
            courtButtons.get(selected).callOnClick();
    }

    public void setButtonStatus(int court, Session session){
        CourtButton cb = courtButtons.get(court);
        if(session.getReservation()!=null)
            cb.setCourtState(CourtButton.CourtState.reserved);
        else{
            cb.setCourtState(CourtButton.CourtState.mac_reserved);
        }
        cb.setTag(session);
    }

    public int getSelected() {
        return selected;
    }
}
