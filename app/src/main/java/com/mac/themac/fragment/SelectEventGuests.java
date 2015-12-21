package com.mac.themac.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.activity.FindEvents;
import com.mac.themac.model.Guest;
import com.mac.themac.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 12/18/2015.
 */
public class SelectEventGuests extends FragmentWithTopActionBar {
    @Bind(R.id.lv_members) ListView lvMembers;
    @Bind(R.id.lv_guests) ListView lvGuests;
    @Bind(R.id.btn_add_guest) Button btnAddGuest;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_select_guests;
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

    @OnClick(R.id.btn_add_guest)
    public void onAddGuestClicked(){
        ((FindEvents)getActivity()).showNewGuest();
    }

    private MembersAdapter mMembersAdapter;
    private GuestsAdapter mGuestsAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMembersAdapter = new MembersAdapter(getActivity(), new ArrayList<User>());
        mMembersAdapter.addAll(TheMACApplication.theApp.getFirebaseHelper().getLoggedInUser().linkedDependents);
        mMembersAdapter.add(TheMACApplication.theApp.getFirebaseHelper().getLoggedInUser());
        mGuestsAdapter = new GuestsAdapter(getActivity(), ((FindEvents)getActivity()).guests);
        lvMembers.setAdapter(mMembersAdapter);
        lvGuests.setAdapter(mGuestsAdapter);
        setHasOptionsMenu(true);
    }

    public void removeGuestAtIndex(int index){
        ((FindEvents)getActivity()).guests.remove(index);
        mGuestsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class GuestSelectRowHolder {
        @Bind(R.id.checkbox) CheckBox checkBox;
        protected int pos;

        GuestSelectRowHolder(View view){
            ButterKnife.bind(this, view);
        }

        public void setPosition(int position){
            pos = position;
        }
    }

    public void openSurvey(boolean isMember, int pos){
        ((FindEvents)getActivity()).showEventSurvey(isMember, pos);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_select_guests_fragment_continue, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_continue){
            ArrayList<String> selectedMembers = new ArrayList<>();
            ArrayList<Integer> selectedMemberIndices = new ArrayList<>();
            for(int i = 0; i < mMembersAdapter.getCount(); i++){
                if(((FindEvents)getActivity()).membersSurveys.get(i, null) != null) {
                    selectedMembers.add(mMembersAdapter.getItem(i).FBKey);
                    selectedMemberIndices.add(i);
                }
            }

            ((FindEvents)getActivity()).showReviewRegistration(selectedMembers, selectedMemberIndices);
        }
        return super.onOptionsItemSelected(item);
    }

    class MembersAdapter extends ArrayAdapter<User>{
        class MemberHolder extends GuestSelectRowHolder{
            private User user;

            MemberHolder(View view) {
                super(view);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            openSurvey(true, pos);
                        }
                    }
                });
            }

            public void setUser(User user){
                this.user = user;
                checkBox.setText(user.firstName + " " + user.lastName);
            }
        }

        public MembersAdapter(Context context, List<User> objects) {
            super(context, -1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MemberHolder holder;
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.guest_select_row, null);
                holder = new MemberHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MemberHolder) convertView.getTag();
            }
            holder.setUser(getItem(position));
            holder.setPosition(position);
            return convertView;
        }
    }

    class GuestsAdapter extends ArrayAdapter<Guest>{
        class GuestHolder extends GuestSelectRowHolder{
            private Guest guest;

            GuestHolder(View view) {
                super(view);
            }

            public void setGuest(Guest guest){
                this.guest = guest;
                checkBox.setText(guest.first + " " + guest.last);
                checkBox.setChecked(true);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            removeGuestAtIndex(pos);
                        }
                    }
                });
            }
        }

        public GuestsAdapter(Context context, ArrayList<Guest> guests) {
            super(context, -1, guests);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GuestHolder holder;
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.guest_select_row, null);
                holder = new GuestHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (GuestHolder) convertView.getTag();
            }
            holder.setGuest(getItem(position));
            holder.setPosition(position);
            return convertView;
        }
    }
}
