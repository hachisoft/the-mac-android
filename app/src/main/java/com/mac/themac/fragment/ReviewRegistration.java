package com.mac.themac.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.activity.FindEvents;
import com.mac.themac.model.Event;
import com.mac.themac.model.Fee;
import com.mac.themac.model.Guest;
import com.mac.themac.model.Registration;
import com.mac.themac.model.SurveyResponse;
import com.mac.themac.model.User;
import com.mac.themac.utility.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 12/19/2015.
 */
public class ReviewRegistration extends FragmentWithTopActionBar {
    @Bind(R.id.cb_agree) CheckBox cbAgree;
    @Bind(R.id.tv_total) TextView tvTotal;
    @Bind(R.id.lv_guests) ListView lvGuests;
    @Bind(R.id.lv_members) ListView lvMembers;
    private static final String ARG_MEMBERS = "members";
    private static final String ARG_INDICES = "indices";

    public static ReviewRegistration newInstance(ArrayList<String> members, ArrayList<Integer> indices){
        ReviewRegistration frag = new ReviewRegistration();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_MEMBERS, members);
        args.putIntegerArrayList(ARG_INDICES, indices);
        frag.setArguments(args);
        return frag;
    }

    @OnClick(R.id.btn_complete)
    public void onCompleteRegistration(){
        if(!cbAgree.isChecked()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Waiver & Consent to Treat");
            builder.setMessage("You must agree with the Waiver and Consent to Treat statement to finish this reservation. To read the agreement, tap on the waiver label. To agree, please check the box.");
            builder.setNeutralButton("Ok", null);
            builder.create().show();
        } else {
            for(int i = 0; i < mMembersAdapter.getCount(); i ++){
                Registration r = forUser(mMembersAdapter.getItem(i));
                SurveyResponse sr = null;
                if(event.getSurvey() != null){
                    sr = ((FindEvents)getActivity()).membersSurveys.get(getArguments().getIntegerArrayList(ARG_INDICES).get(i));
                }
                pushRegistrationToDB(r, sr);
            }
            for(int i = 0; i < mGuestsAdapter.getCount(); i++){
                Registration r = forGuest(mGuestsAdapter.getItem(i));
                pushRegistrationToDB(r, mGuestsAdapter.getItem(i).surveyResponse);
            }
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); i++)
            fm.popBackStack();
    }

    private void pushRegistrationToDB(Registration r, SurveyResponse sr){
        Firebase registrationRef = TheMACApplication.theApp.getFirebaseHelper().getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.registrations);
        Firebase newRegistationRef = registrationRef.push();
        newRegistationRef.setValue(r);
        String key = newRegistationRef.getKey();
        event.getRegistrations().put(key, true);
        Firebase eventRef = TheMACApplication.theApp.getFirebaseHelper().getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.events).child(event.FBKey);
        eventRef.setValue(event);
        if(sr != null){
            sr.registration = r.FBKey;
            Firebase srRef = TheMACApplication.theApp.getFirebaseHelper().getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.surveyResponses);
            Firebase newSrRef = srRef.push();
            newSrRef.setValue(sr);
            if(((FindEvents)getActivity()).getCurrentSurvey().responses == null)
                ((FindEvents)getActivity()).getCurrentSurvey().responses = new HashMap<>();
            ((FindEvents)getActivity()).getCurrentSurvey().responses.put(newSrRef.getKey(), true);
            Firebase surveyRef = TheMACApplication.theApp.getFirebaseHelper().getRootKeyedObjectRef(FirebaseHelper.FBRootContainerNames.surveys).child(sr.getSurvey());
            surveyRef.setValue((((FindEvents) getActivity()).getCurrentSurvey()));
        }
    }

    private Registration generateRegistration(){
        Registration r = new Registration();
        r.memberNumber = TheMACApplication.theApp.getFirebaseHelper().getLoggedInUser().memberNumber;
        r.registeringUser = TheMACApplication.theApp.getFirebaseHelper().getLoggedInUser().FBKey;
        r.event = event.FBKey;
        r.isOnWaitlist = false; //TODO need to add possibility of waitlist
        r.status = "Reserved"; //TODO need to add possibility of waitlist
        //TODO do we need to set something for group?
        return r;
    }

    private Registration forUser(User user){
        Registration r = generateRegistration();
        r.firstName = user.firstName;
        r.lastName = user.lastName;
        r.isGuest = false;
        r.isJunior = user.isJunior;
        r.registeredUser = user.FBKey;
        if(feeHashMap.containsKey("All")){
            r.fee = feeHashMap.get("All").FBKey;
        }
        else if(r.isJunior){
            if(feeHashMap.containsKey("Junior"))
                r.fee = feeHashMap.get("Junior").FBKey;
            else if(feeHashMap.containsKey("Member"))
                r.fee = feeHashMap.get("Member").FBKey;
        } else {
            if(feeHashMap.containsKey("Member"))
                r.fee = feeHashMap.get("Member").FBKey;
        }
        return r;
    }

    private Registration forGuest(Guest guest){
        Registration r = generateRegistration();
        r.firstName = guest.first;
        r.lastName = guest.last;
        r.isGuest = true;
        r.isJunior = guest.isChild;
        if(feeHashMap.containsKey("All")){
            r.fee = feeHashMap.get("All").FBKey;
        } else if(r.isJunior){
            if(feeHashMap.containsKey("GuestJunior"))
                r.fee = feeHashMap.get("GuestJunior").FBKey;
            else if(feeHashMap.containsKey("Guest"))
                r.fee = feeHashMap.get("Guest").FBKey;
            else if(feeHashMap.containsKey("Junior"))
                r.fee = feeHashMap.get("Junior").FBKey;
            else if(feeHashMap.containsKey("Member"))
                r.fee = feeHashMap.get("Member").FBKey;
        } else {
            if(feeHashMap.containsKey("Guest"))
                r.fee = feeHashMap.get("Guest").FBKey;
            else if(feeHashMap.containsKey("Member"))
                r.fee = feeHashMap.get("Member").FBKey;
        }
        return r;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_review_registration;
    }

    @Override
    protected int getTitleResourceId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMembersAdapter = new MembersAdapter(getActivity());
        mGuestsAdapter = new GuestsAdapter(getActivity());
        mGuestsAdapter.addAll(((FindEvents)getActivity()).guests);
        event = ((FindEvents)getActivity()).getCurrentEvent();
        for(Fee fee: event.linkedFees){
            feeHashMap.put(fee.type, fee);
        }
        for(String s: getArguments().getStringArrayList(ARG_MEMBERS)){
            if(s.equals(TheMACApplication.theApp.getFirebaseHelper().getLoggedInUser().FBKey)){
                mMembersAdapter.add(TheMACApplication.theApp.getFirebaseHelper().getLoggedInUser());
            } else {
                for(User user: TheMACApplication.theApp.getFirebaseHelper().getLoggedInUser().linkedDependents){
                    if(s.equals(user.FBKey))
                        mMembersAdapter.add(user);
                }
            }
            mMembersAdapter.notifyDataSetChanged();
        }
        lvMembers.setAdapter(mMembersAdapter);
        lvGuests.setAdapter(mGuestsAdapter);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    private MembersAdapter mMembersAdapter;
    private GuestsAdapter mGuestsAdapter;
    private Event event;
    private HashMap<String, Fee> feeHashMap = new HashMap<>();

    public class RowHolder {
        @Bind(R.id.tv_name) TextView name;
        @Bind(R.id.tv_cost) TextView total;

        RowHolder(View view){
            ButterKnife.bind(this, view);
        }

        public void setGuest(Guest guest){
            name.setText(guest.first + " " + guest.last);
        }

        public void setUser(User user){
            name.setText(user.firstName + " " + user.lastName);
        }
    }

    public class MembersAdapter extends ArrayAdapter<User>{

        public MembersAdapter(Context context) {
            super(context, -1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RowHolder holder;
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.registration_review_row, null);
                holder = new RowHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RowHolder) convertView.getTag();
            }
            holder.setUser(getItem(position));
            return convertView;
        }
    }

    public class GuestsAdapter extends ArrayAdapter<Guest>{

        public GuestsAdapter(Context context) {
            super(context, -1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RowHolder holder;
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.registration_review_row, null);
                holder = new RowHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (RowHolder) convertView.getTag();
            }
            holder.setGuest(getItem(position));
            return convertView;
        }
    }
}
