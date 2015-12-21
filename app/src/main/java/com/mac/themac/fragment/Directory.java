package com.mac.themac.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.adapter.DirectoryAdapter;
import com.mac.themac.model.Department;
import com.mac.themac.model.EmployeeProfile;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Directory extends FragmentWithTopActionBar implements FBChildListener {
    @Bind(R.id.list_view) ListView listView;
    @Bind(R.id.rb_alphabetical) RadioButton alphabetical;
    @Bind(R.id.rb_departments) RadioButton departments;
    private FirebaseHelper _FBHelper;
    private DirectoryAdapter mAdapter;

    @OnClick(R.id.rb_departments)
    public void onDepartmentsSelected(){
        mAdapter.setSortType(DirectoryAdapter.SortType.department);
    }

    @OnClick(R.id.rb_alphabetical)
    public void onAlphabeticalSelected(){
        mAdapter.setSortType(DirectoryAdapter.SortType.alphabetical);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Profile.
     */
    public static Directory newInstance() {
        Directory fragment = new Directory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Directory() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_directory;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.directory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        mAdapter = new DirectoryAdapter(getActivity());
        departments.setChecked(true);
        listView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _FBHelper = TheMACApplication.theApp.getFirebaseHelper();
        _FBHelper.SubscribeToChildUpdates(this, Department.class, new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "name"));
        _FBHelper.SubscribeToChildUpdates(this, EmployeeProfile.class, new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "department"));
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {
        if(modelIdentifier.IsIntendedObject(model, Department.class)) {
            Department department = (Department) model;
            mAdapter.addObject(department);
        } else if(modelIdentifier.IsIntendedObject(model, EmployeeProfile.class)) {
            EmployeeProfile ep = (EmployeeProfile) model;
            mAdapter.addObject(ep);
        }
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
}
