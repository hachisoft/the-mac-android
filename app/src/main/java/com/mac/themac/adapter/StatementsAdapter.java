package com.mac.themac.adapter;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mac.themac.R;
import com.mac.themac.activity.ActivityWithBottomActionBar;
import com.mac.themac.activity.MyAccount;
import com.mac.themac.fragment.AccountStatement;
import com.mac.themac.model.Statement;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samir on 10/12/2015.
 */
public class StatementsAdapter extends ArrayAdapter<Statement> {

    public StatementsAdapter(Context context, int resource, List<Statement> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Statement statement = getItem(position);

        StatementViewHolder holder;
        if(convertView != null){
            holder = (StatementViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_statement, parent, false);
            holder = new StatementViewHolder(convertView);
            convertView.setTag(holder);
        }

        try {
            holder._date.setText(DateFormat.getDateInstance(DateFormat.LONG).format(statement.date));
        }
        catch(Exception x){
            holder._date.setText("Not Available");
        }
        DecimalFormat df = new DecimalFormat("#.00");
        holder._amount.setText("$ " + df.format(statement.currentBalance));
        statement.loadLinkedObjects();
        try {
            final MyAccount activity = (MyAccount) getContext();
            if(activity != null){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AccountStatement frag = new AccountStatement();
                        frag.setStatement(statement);
                        activity.showFragment(frag, R.id.myAccountContainer,
                                ActivityWithBottomActionBar.TransitionEffect.Slide_from_right);
                    }
                });
            }
        }
        catch(Exception x){

        }
        return convertView;
    }

    public class StatementViewHolder {

        @Bind(R.id.date)
        TextView _date;
        @Bind(R.id.amount)
        TextView _amount;

        public StatementViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
