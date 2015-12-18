package com.mac.themac.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mac.themac.R;
import com.mac.themac.adapter.StatementsAdapter;
import com.mac.themac.adapter.TransactionsAdapter;
import com.mac.themac.model.Statement;
import com.mac.themac.model.Transaction;
import com.mac.themac.model.User;
import com.mac.themac.utility.DownloadImageTask;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountStatement extends FragmentWithTopActionBar {

    @Bind(R.id.editStatementDate) EditText _statementDate;
    @Bind(R.id.editDueDate) EditText _dueDate;
    @Bind(R.id.editBalDue) EditText _balDue;
    @Bind(R.id.editBalForward) EditText _balForward;
    //@Bind(R.id.listViewTransactions) ListView _lvTransactions;
    @Bind(R.id.editCurrent) EditText _current;
    @Bind(R.id.editOver30) EditText _over30;
    @Bind(R.id.editOver60) EditText _over60;
    @Bind(R.id.editOver90) EditText _over90;
    @Bind(R.id.editOver120) EditText _over120;
    @Bind(R.id.transactions) LinearLayout _transactions;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Statement mStatement;
    //private TransactionsAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        //_adapter = new TransactionsAdapter(this.getActivity(), R.layout.listitem_transaction, mStatement.linkedTransactions);
        //_lvTransactions.setAdapter(_adapter);
        updateUIFromModel();
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

    public AccountStatement() {
        // Required empty public constructor
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_statement;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.my_statement;
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


    public void setStatement(Statement statement) {
        this.mStatement = statement;
    }

    private void updateUIFromModel(){

        if(mStatement != null){

            if(mStatement.date != null)
                _statementDate.setText(DateFormat.getDateInstance(DateFormat.LONG).format(mStatement.date));
            if(mStatement.dueDate != null)
                _dueDate.setText(DateFormat.getDateInstance(DateFormat.LONG).format(mStatement.dueDate));

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            _balDue.setText("$ " + decimalFormat.format(mStatement.currentBalance));
            _balForward.setText("$ " + decimalFormat.format(mStatement.balanceForward));
            _current.setText("$ " + decimalFormat.format(mStatement.currentBalance));
            _over30.setText("$ " + decimalFormat.format(mStatement.over30));
            _over60.setText("$ " + decimalFormat.format(mStatement.over60));
            _over90.setText("$ " + decimalFormat.format(mStatement.over90));
            _over120.setText("$ " + decimalFormat.format(mStatement.over120));

            Collections.sort(mStatement.linkedTransactions, new Comparator<Transaction>() {
                @Override
                public int compare(Transaction lhs, Transaction rhs) {
                    return lhs.date.compareTo(rhs.date);
                }
            });

            TransactionsAdapter adapter = new TransactionsAdapter(this.getActivity(),
                    R.layout.listitem_transaction, mStatement.linkedTransactions);

            for(int i = 0; i < adapter.getCount(); i++) {
                View v = adapter.getView(i, null, _transactions );
                _transactions.addView(v);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
