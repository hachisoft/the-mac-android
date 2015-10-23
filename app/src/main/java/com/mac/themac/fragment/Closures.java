package com.mac.themac.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.Closure;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;
import com.mac.themac.utility.FirebaseHelper;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Closures extends FragmentWithTopActionBar implements FBChildListener {
    private ClosureAdapter mAdapter;
    private FirebaseHelper _FBHelper;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Closures newInstance() {
        Closures fragment = new Closures();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Closures() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_closure;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.facility_closures;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ClosureAdapter(getActivity());
        ListView list = (ListView) getView();
        list.setAdapter(mAdapter);
        _FBHelper = TheMACApplication.theApp.getFirebaseHelper();
        _FBHelper.SubscribeToChildUpdates(this, Closure.class, new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "startDate"));
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {
        if(modelIdentifier.IsIntendedObject(model, Closure.class)) {
            Closure closure = (Closure) model;
            mAdapter.add(closure);
            mAdapter.notifyDataSetChanged();
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

    class ClosureAdapter extends ArrayAdapter<Closure>{
        SimpleDateFormat dateDisplay = new SimpleDateFormat("MM/dd hh:mm aa");
        class ViewHolder{
            @Bind(R.id.tv_title) TextView title;
            @Bind(R.id.tv_time) TextView time;
            @Bind(R.id.tv_description) TextView description;

            public ViewHolder(View view){
                ButterKnife.bind(this, view);
            }
        }
        public ClosureAdapter(Context context) {
            super(context, -1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(getContext(), R.layout.closure_list_row, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(getItem(position).getTitle());
            holder.description.setText(getItem(position).getDescription());
            holder.time.setText("Closed: " + dateDisplay.format(getItem(position).getStartDate()) + " to: " + dateDisplay.format(getItem(position).getEndDate()));
            return convertView;
        }
    }

}
