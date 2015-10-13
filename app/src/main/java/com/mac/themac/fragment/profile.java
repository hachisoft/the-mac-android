package com.mac.themac.fragment;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mac.themac.R;
import com.mac.themac.model.User;
import com.mac.themac.utility.DownloadImageTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

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

    User mUser;

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
        if(mUser.dob != null)
            c.setTime(mUser.dob);

        mDOBPickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar dob = Calendar.getInstance();
                dob.set(year, monthOfYear, dayOfMonth);
                mUser.dob = dob.getTime();
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                _dob.setText(dateFormat.format(mUser.dob));
                mFBHelper.setFBModelValue(mUser.FBKey, mUser);
                _phone.requestFocus();
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        mDOBPickerDialog.setTitle("Pick Date of Birth");
        mDOBPickerDialog.show();
    }

    /*Keeping profile page read only
    @OnFocusChange({R.id.editName, R.id.selGender,R.id.pickDateOfBirth, R.id.editPhone,
            R.id.editEmail, R.id.editOccupation, R.id.editAddress, R.id.editOfficeAddress})
    public void updateModelFromUI(View v, boolean hasFocus){

        if(hasFocus == false && mUser != null) {//update  on lost focus
            switch (v.getId()) {
                case R.id.editName:
                    String[] names = _name.getText().toString().split(" ", 3);
                    if (names.length == 0) {
                        mUser.firstName = "";
                        mUser.middleName = "";
                        mUser.lastName = "";
                    } else if (names.length == 1) {
                        mUser.firstName = names[0];
                        mUser.middleName = "";
                        mUser.lastName = "";
                    } else if (names.length == 2) {
                        mUser.firstName = names[0];
                        mUser.lastName = names[1];
                        mUser.middleName = "";
                    } else {
                        mUser.firstName = names[0];
                        mUser.middleName = names[1];
                        mUser.lastName = names[2];
                    }
                    _name.clearFocus();
                    _gender.requestFocus();
                    _gender.performClick();
                    break;
                case R.id.selGender:
                    switch (_gender.getSelectedItemPosition()){
                        case 0:
                            mUser.gender = "Male";
                            break;
                        case 1:
                            mUser.gender = "Female";
                            break;
                        default:
                            mUser.gender = "Unspecified";
                            break;
                    }
                    break;
                case R.id.editPhone:
                    mUser.homePhone = _phone.getText().toString();
                    break;
                case R.id.editEmail:
                    mUser.email = _email.getText().toString();
                    break;
                case R.id.editOccupation:
                    mUser.occupation = _occupation.getText().toString();
                    break;
                case R.id.editAddress:

                    break;
                case R.id.editOfficeAddress:

                    break;
            }
            mFBHelper.setFBModelValue(mUser.FBKey, mUser);
        }
        else if(hasFocus && mUser != null){
            switch (v.getId()) {

                case R.id.pickDateOfBirth:
                    showDOBPicker();
                    break;
            }
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        User loggedInUser = mFBHelper.getLoggedInUser();
        updateUIFromModel(loggedInUser);
        /*_gender.setFocusable(true);
        _gender.setFocusableInTouchMode(true);
        */
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

    private void updateUIFromModel(User user){

        mUser = user;
        if (user.thumbId != null && user.thumbId.length() > 0) {
            try {
                Uri imageUri = Uri.parse(user.thumbId);
                if (imageUri != null) {
                    new DownloadImageTask(_profilePic).execute(user.thumbId);
                }
            }
            catch (Exception x){}//ignore loading profile pic for now
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

        _name.setText(user.firstName + " " +
                        user.middleName + " " +
                        user.lastName);
        _dob.setText(dateFormat.format(user.dob));
        _email.setText(user.email);
        _gender.setSelection(user.gender.equalsIgnoreCase("Male") ?
                0 : (user.gender.equalsIgnoreCase("Female") ? 1 : 2));
        _memberId.setText("Member: " + user.memberNumber);
        _memberSince.setText("Since: " + dateFormat.format(user.memberSince));
        _memberStatus.setText("Status: " + user.status);
        _phone.setText(user.homePhone);
        _occupation.setText(user.occupation);
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
