package com.mac.themac.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.fragment.AccountStatement;
import com.mac.themac.fragment.CurrentCharges;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.fragment.Interests;
import com.mac.themac.fragment.Profile;
import com.mac.themac.fragment.Reservations;
import com.mac.themac.model.MemberProfile;
import com.mac.themac.model.User;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.DownloadImageTask;
import com.mac.themac.utility.FirebaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccount extends ActivityWithBottomActionBar implements
        FragmentWithTopActionBar.OnFragmentInteractionListener, FBModelListener{

    @Bind(R.id.profileImg) ImageView _profilePic;
    @Bind(R.id.txtMemberId) TextView _memberId;
    @Bind(R.id.txtMemberSince) TextView _memberSince;
    @Bind(R.id.txtStatus) TextView _memberStatus;

    @OnClick(R.id.btnMyProfile)
    public void showMyProfile(View v){

        Profile profileFragment = new Profile();
        showFragment(profileFragment, R.id.myAccountContainer);
    }

    @OnClick(R.id.btnMyInterests)
    public void showMyInterests(){
        showFragment(new Interests(), R.id.myAccountContainer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        User loggedInUser = _FBHelper.getLoggedInUser();
        if(loggedInUser.linkedMemberProfile == null){
            _FBHelper.SubscribeToModelUpdates(this, MemberProfile.class,loggedInUser.memberProfile);
        }
        else{
            updateUIFromModel(loggedInUser.linkedMemberProfile);
        }
    }

    private void updateUIFromModel(MemberProfile memberProfile) {

        if (memberProfile.thumbId != null && memberProfile.thumbId.length() > 0) {
            try {
                Uri imageUri = Uri.parse(memberProfile.thumbId);
                if (imageUri != null) {
                    new DownloadImageTask(_profilePic).execute(memberProfile.thumbId);
                }

            }
            catch (Exception x){}//ignore loading profile pic for now
        }
        _memberId.setText("Member: " + memberProfile.number);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        _memberSince.setText("Since: " + dateFormat.format(memberProfile.memberSince));
        _memberStatus.setText("Status: " + memberProfile.status);

    }

    @OnClick(R.id.btnMyReservations)
    public void showMyReservations(){
        showFragment(new Reservations(), R.id.myAccountContainer);
    }

    @OnClick(R.id.btnCurrentCharges)
    public void showCurrentCharges(){
        showFragment(new CurrentCharges(), R.id.myAccountContainer);
    }

    @OnClick(R.id.btnMyStatement)
    public void showMyStatement(){
        showFragment(new AccountStatement(), R.id.myAccountContainer);
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.activity_my_account;
    }

    @Override
    ToggleButton getActiveButton() {
        return _btnAccount;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDataChange(FBModelIdentifier identifier, FBModelObject model) {
        if(identifier.IsIntendedObject(model, MemberProfile.class))
        {
            MemberProfile profile = (MemberProfile)model;
            updateUIFromModel(profile);
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
