package com.mac.themac.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.FirebaseError;
import com.mac.themac.R;
import com.mac.themac.adapter.TransactionsAdapter;
import com.mac.themac.model.Transaction;
import com.mac.themac.model.User;
import com.mac.themac.model.Vehicle;
import com.mac.themac.model.firebase.FBChildListener;
import com.mac.themac.model.firebase.FBModelIdentifier;
import com.mac.themac.model.firebase.FBModelObject;
import com.mac.themac.model.firebase.FBQueryIdentifier;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentCharges extends FragmentWithTopActionBar implements FBChildListener {

    @Bind(R.id.listViewTransactions)
    ListView _lvTransactions;
    @Bind(R.id.editASOf)
    EditText _asof;
    @Bind(R.id.editCurBal)
    EditText _curBalance;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TransactionsAdapter _adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        _asof.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(new Date()));
        if(mFBHelper.getLoggedInUser() != null && mFBHelper.getLoggedInUser().linkedTransactions != null) {

            Collections.sort(mFBHelper.getLoggedInUser().linkedTransactions, new Comparator<Transaction>() {
                @Override
                public int compare(Transaction lhs, Transaction rhs) {
                    return lhs.date.compareTo(rhs.date);
                }
            });

            _adapter = new TransactionsAdapter(this.getActivity(), R.layout.listitem_transaction, mFBHelper.getLoggedInUser().linkedTransactions);
            _lvTransactions.setAdapter(_adapter);

            double dAmount = 0;
            for(Transaction t : mFBHelper.getLoggedInUser().linkedTransactions) {
                if (t.status.compareTo(Transaction.STATUS.Pending.name()) == 0) {
                    dAmount += t.amount;
                }
            }
            _curBalance.setText("$ " + new DecimalFormat("#.00").format(dAmount));
        }

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
    public static CurrentCharges newInstance(String param1, String param2) {
        CurrentCharges fragment = new CurrentCharges();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CurrentCharges() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_current_charges;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.current_charges;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onChildAdded(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String prevChild) {
        if(modelIdentifier.IsIntendedObject(model, Transaction.class)){
            if(_adapter == null){
                if(mFBHelper.getLoggedInUser().linkedTransactions != null){
                    _adapter = new TransactionsAdapter(this.getActivity(), R.layout.listitem_transaction, mFBHelper.getLoggedInUser().linkedTransactions);
                    _lvTransactions.setAdapter(_adapter);
                }
            }
            Transaction transaction = (Transaction)model;
            mFBHelper.getLoggedInUser().linkedTransactions.add(transaction);
            double dAmount = 0;
            for(Transaction t : mFBHelper.getLoggedInUser().linkedTransactions) {
                if (t.status.compareTo(Transaction.STATUS.Pending.name()) == 0) {
                    dAmount += t.amount;
                }
            }
            _curBalance.setText("$ " + new DecimalFormat("#.00").format(dAmount));
            _adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key) {
        if(_adapter != null)
            _adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model) {
        if(_adapter != null)
            _adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(FBModelIdentifier modelIdentifier, FBQueryIdentifier queryIdentifier, FBModelObject model, String key) {
        if(_adapter != null)
            _adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancel(FBModelIdentifier identifier, FirebaseError error) {
        if(_adapter != null)
            _adapter.notifyDataSetChanged();
    }

    @Override
    public void onNullData(FBModelIdentifier identifier, String key) {
        if(_adapter != null)
            _adapter.notifyDataSetChanged();
    }

    @Override
    public void onException(Exception x) {
        if(_adapter != null)
            _adapter.notifyDataSetChanged();
    }
}
