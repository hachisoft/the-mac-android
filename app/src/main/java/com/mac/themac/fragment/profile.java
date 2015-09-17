package com.mac.themac.fragment;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mac.themac.R;
import com.mac.themac.model.MemberProfile;
import com.mac.themac.model.User;
import com.mac.themac.utility.DownloadImageTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTouch;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends FragmentWithTopActionBar {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatePickerDialog mDOBPickerDialog;

    MemberProfile mMemberProfile;

    @Bind(R.id.profileImg) ImageView _profilePic;
    @Bind(R.id.txtMemberId) TextView _memberId;
    @Bind(R.id.txtMemberSince) TextView _memberSince;
    @Bind(R.id.txtStatus) TextView _memberStatus;
    @Bind(R.id.editName) EditText _name;
    @Bind(R.id.selGender) Spinner _gender;
    @Bind(R.id.pickDateOfBirth) EditText _dob;
    @Bind(R.id.editPhone) EditText _phone;
    @Bind(R.id.editEmail) EditText _email;
    @Bind(R.id.editOccupation) EditText _occupation;
    @Bind(R.id.editOfficeAddress) EditText _officeAddress;

    public void showDOBPicker(){
        Calendar c = Calendar.getInstance();
        if(mMemberProfile.dob != null)
            c.setTime(mMemberProfile.dob);

        mDOBPickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar dob = Calendar.getInstance();
                dob.set(year, monthOfYear, dayOfMonth);
                mMemberProfile.dob = dob.getTime();
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                _dob.setText(dateFormat.format(mMemberProfile.dob));
                mFBHelper.setFBModelValue(mMemberProfile.FBKey, mMemberProfile);
                _phone.requestFocus();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        mDOBPickerDialog.setTitle("Pick Date of Birth");
        mDOBPickerDialog.show();
    }

    @OnFocusChange({R.id.editName, R.id.selGender,R.id.pickDateOfBirth, R.id.editPhone,
            R.id.editEmail, R.id.editOccupation, R.id.editAddress, R.id.editOfficeAddress})
    public void updateModelFromUI(View v, boolean hasFocus){

        if(hasFocus == false && mMemberProfile != null) {//update  on lost focus
            switch (v.getId()) {
                case R.id.editName:
                    String[] names = _name.getText().toString().split(" ", 3);
                    if (names.length == 0) {
                        mMemberProfile.firstName = "";
                        mMemberProfile.middleName = "";
                        mMemberProfile.lastName = "";
                    } else if (names.length == 1) {
                        mMemberProfile.firstName = names[0];
                        mMemberProfile.middleName = "";
                        mMemberProfile.lastName = "";
                    } else if (names.length == 2) {
                        mMemberProfile.firstName = names[0];
                        mMemberProfile.lastName = names[1];
                        mMemberProfile.middleName = "";
                    } else {
                        mMemberProfile.firstName = names[0];
                        mMemberProfile.middleName = names[1];
                        mMemberProfile.lastName = names[2];
                    }
                    _name.clearFocus();
                    _gender.requestFocus();
                    _gender.performClick();
                    break;
                case R.id.selGender:
                    switch (_gender.getSelectedItemPosition()){
                        case 0:
                            mMemberProfile.gender = "Male";
                            break;
                        case 1:
                            mMemberProfile.gender = "Female";
                            break;
                        default:
                            mMemberProfile.gender = "Unspecified";
                            break;
                    }
                    break;
                case R.id.editPhone:
                    mMemberProfile.homePhone = _phone.getText().toString();
                    break;
                case R.id.editEmail:
                    mMemberProfile.email = _email.getText().toString();
                    break;
                case R.id.editOccupation:
                    mMemberProfile.occupation = _occupation.getText().toString();
                    break;
                case R.id.editAddress:

                    break;
                case R.id.editOfficeAddress:

                    break;
            }
            mFBHelper.setFBModelValue(mMemberProfile.FBKey, mMemberProfile);
        }
        else if(hasFocus && mMemberProfile != null){
            switch (v.getId()) {

                case R.id.pickDateOfBirth:
                    showDOBPicker();
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        User loggedInUser = mFBHelper.getLoggedInUser();
        if(loggedInUser.linkedMemberProfile == null){
            loggedInUser.loadMemberProfileInterestsRegistrations();
        }
        else{
            updateUIFromModel(loggedInUser.linkedMemberProfile);
        }
        _gender.setFocusable(true);
        _gender.setFocusableInTouchMode(true);

        return view;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.my_profile;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void updateUIFromModel(MemberProfile memberProfile){

        mMemberProfile = memberProfile;
        if (memberProfile.thumbId != null && memberProfile.thumbId.length() > 0) {
            try {
                Uri imageUri = Uri.parse(memberProfile.thumbId);
                if (imageUri != null) {
                    new DownloadImageTask(_profilePic).execute(memberProfile.thumbId);
                }
            }
            catch (Exception x){}//ignore loading profile pic for now
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

        _name.setText(memberProfile.firstName + " " +
                        memberProfile.middleName + " " +
                        memberProfile.lastName);
        _dob.setText(dateFormat.format(memberProfile.dob));
        _email.setText(memberProfile.email);
        _gender.setSelection(memberProfile.gender.equalsIgnoreCase("Male") ?
                0 : (memberProfile.gender.equalsIgnoreCase("Female") ? 1 : 2));
        _memberId.setText("Member: " + memberProfile.number);
        _memberSince.setText("Since: " + dateFormat.format(memberProfile.memberSince));
        _memberStatus.setText("Status: " + memberProfile.status);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

}
