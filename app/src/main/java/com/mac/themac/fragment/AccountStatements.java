package com.mac.themac.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mac.themac.R;
import com.mac.themac.adapter.StatementsAdapter;
import com.mac.themac.model.Statement;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samir on 10/12/2015.
 */
public class AccountStatements extends FragmentWithTopActionBar {

    @Bind(R.id.listViewStatements)
    ListView _lvStatements;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private StatementsAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        if(mFBHelper.getLoggedInUser() != null && mFBHelper.getLoggedInUser().linkedTransactions != null) {

            Collections.sort(mFBHelper.getLoggedInUser().linkedStatements, new Comparator<Statement>() {
                @Override
                public int compare(Statement lhs, Statement rhs) {
                    return rhs.date.compareTo(lhs.date);
                }
            });

            _adapter = new StatementsAdapter(this.getActivity(), R.layout.listitem_statement, mFBHelper.getLoggedInUser().linkedStatements);
            _lvStatements.setAdapter(_adapter);
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
    public static AccountStatement newInstance(String param1, String param2) {
        AccountStatement fragment = new AccountStatement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountStatements() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_statements;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.my_statements;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
