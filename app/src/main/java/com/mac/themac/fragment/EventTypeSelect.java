package com.mac.themac.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.TheMACApplication;
import com.mac.themac.model.Interest;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;

import java.util.ArrayList;

/**
 * Created by Bryan on 9/14/2015.
 */
public class EventTypeSelect extends FragmentWithTopActionBar implements FBChildListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private ArrayList<String> selected;
    private EventTypesAdapter mAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static EventTypeSelect newInstance(ArrayList<String> param1) {
        EventTypeSelect fragment = new EventTypeSelect();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public EventTypeSelect() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_select_event_types;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.event_types;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mAdapter = new EventTypesAdapter(getActivity(), R.layout.interest_select_row);
        ((ListView)getView()).setAdapter(mAdapter);
        TheMACApplication.theApp.getFirebaseHelper().SubscribeToChildUpdates(this, Interest.class,
                new FBQueryIdentifier(FBQueryIdentifier.OrderBy.Child, "name"));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {
        if(modelIdentifier.IsIntendedObject(model, Interest.class)) {
            Interest interest = (Interest) model;
            mAdapter.add(interest);
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

    public class EventTypesAdapter extends ArrayAdapter<Interest>{
        public SparseArray<Boolean> checked = new SparseArray<>();
        public EventTypesAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            if(view == null){
                view = LinearLayout.inflate(getContext(), R.layout.interest_select_row, null);
            } else {
                Integer lastPos = (Integer) view.getTag();
                CheckBox cb = (CheckBox) view.findViewById(R.id.cb_interest);
                if(cb.isChecked())
                    checked.put(lastPos, true);
                else{
                    if(checked.get(lastPos)!=null)
                        checked.removeAt(lastPos);
                }
            }
            CheckBox cb = (CheckBox) view.findViewById(R.id.cb_interest);
            cb.setText(getItem(position).name);
            cb.setChecked(checked.get(position, false));
            view.setTag(position);
            return view;
        }

        @Override
        public void add(Interest interest){
            super.add(interest);
            if(selected.contains(interest.FBKey))
                checked.put(getCount()-1, true);
        }
    }
}
