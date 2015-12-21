package com.mac.themac.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.fragment.AccountStatements;
import com.mac.themac.fragment.CurrentCharges;
import com.mac.themac.fragment.FragmentWithTopActionBar;
import com.mac.themac.fragment.Profile;
import com.mac.themac.fragment.Reservations;
import com.mac.themac.model.User;
import com.mac.themac.model.firebase.FBModelListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.utility.DownloadImageTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccount extends ActivityWithBottomActionBar implements
        FragmentWithTopActionBar.OnFragmentInteractionListener, FBModelListener {

    @Bind(R.id.profileImg) ImageView _profilePic;
    @Bind(R.id.txtMemberId) TextView _memberId;
    @Bind(R.id.txtMemberSince) TextView _memberSince;
    @Bind(R.id.txtStatus) TextView _memberStatus;

    @OnClick(R.id.btnMyProfile)
    public void showMyProfile(View v){

        Profile profileFragment = new Profile();
        showFragment(profileFragment, R.id.myAccountContainer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        User loggedInUser = _FBHelper.getLoggedInUser();
        updateUIFromModel(loggedInUser);
    }

    private void updateUIFromModel(User user) {

        if (user.thumbId != null && user.thumbId.length() > 0) {
            try {
                Uri imageUri = Uri.parse(user.thumbId);
                if (imageUri != null) {
                    new DownloadImageTask(_profilePic).execute(user.thumbId);
                }

            }
            catch (Exception x){}//ignore loading profile pic for now
        }
        _memberId.setText("Member: " + user.memberNumber);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        _memberSince.setText("Since: " + dateFormat.format(user.memberSince));
        _memberStatus.setText("Status: " + user.status);

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
        showFragment(new AccountStatements(), R.id.myAccountContainer);
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
