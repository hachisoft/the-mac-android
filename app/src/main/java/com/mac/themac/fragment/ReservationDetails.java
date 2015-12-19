package com.mac.themac.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.activity.FindEvents;
import com.mac.themac.model.Reservation;
import com.mac.themac.model.ReservationAsset;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.ModelListener;
import com.mac.themac.utility.FirebaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Samir on 12/18/2015.
 */
public class ReservationDetails extends FragmentWithTopActionBar implements FBModelListener {
    @Bind(R.id.tv_title) TextView tvTitle;
    @Bind(R.id.tv_ball_machine) TextView tvBallMachine;
    @Bind(R.id.tv_dates) TextView tvDates;
    @Bind(R.id.tv_description) TextView tvDescription;
    @Bind(R.id.indicator)
    ImageView ivIndicator;

    private static String ARG_RESERVATION_KEY = "reservation_key";
    private String reservationKey;
    private Reservation reservation;

    public static ReservationDetails newInstance(String reservationKey){
        ReservationDetails frag = new ReservationDetails();
        Bundle args = new Bundle();
        args.putString(ARG_RESERVATION_KEY, reservationKey);
        frag.setArguments(args);
        return frag;
    }

    public ReservationDetails(){}

    private void toViews(){

        if(reservation.linkedLocation != null) {
            tvTitle.setText(reservation.linkedLocation.name);
            tvDescription.setText(reservation.linkedLocation.description);
        }

        if(reservation.linkedSession != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            tvDates.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(reservation.linkedSession.date) +
                    " " + timeFormat.format(reservation.linkedSession.date) + " - " +
                    " " + timeFormat.format(reservation.linkedSession.endDate));
        }
        if(reservation.status.equals("Reserved"))
            ivIndicator.setImageResource(R.drawable.status_reserved);
        else if(reservation.status.equals("SoldOut"))
            ivIndicator.setImageResource(R.drawable.status_sold_out);
        else if(reservation.status.equals("Unavailable"))
            ivIndicator.setImageResource(R.drawable.status_unavailable);
        else if(reservation.status.equals("Waitlist"))
            ivIndicator.setImageResource(R.drawable.status_waitlist);
        else if(reservation.status.equals("NoRegistrationRequired"))
            ivIndicator.setImageResource(R.drawable.status_no_registration);

        if(reservation.asset != null && !reservation.asset.isEmpty()) {
            reservation.setModelUpdateListener(new ModelListener() {
                @Override
                public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
                    if(model instanceof ReservationAsset) {
                        tvBallMachine.post(new Runnable() {
                            @Override
                            public void run() {
                                tvBallMachine.setText(reservation.linkedAsset == null ? "No Ball Machine" : reservation.linkedAsset.name);
                            }
                        });
                    }
                }
            });
            reservation.loadLinkedAsset();
        }
        else{
            tvBallMachine.setText("No Ball Machine");
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            reservationKey = getArguments().getString(ARG_RESERVATION_KEY);
        }
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_reservation_details;
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
        fbHelper.SubscribeToModelUpdates(this, new FBModelIdentifier(Reservation.class), reservationKey);
    }


    @Override
    protected int getTitleResourceId() {
        return R.string.reservation_info;
    }

    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
        if(identifier.IsIntendedObject(model, Reservation.class)) {
            reservation = (Reservation) model;
            reservation.loadLinkedLocationAndSession();
            toViews();
        }
    }

    @OnClick(R.id.btnRegister)
    public void onRegisterClicked(){

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
