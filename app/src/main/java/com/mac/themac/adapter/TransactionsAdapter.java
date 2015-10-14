package com.mac.themac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mac.themac.R;
import com.mac.themac.model.Transaction;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Samir on 10/12/2015.
 */
public class TransactionsAdapter extends ArrayAdapter<Transaction> {

    public TransactionsAdapter(Context context, int resource, List<Transaction> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Transaction transaction = getItem(position);

        TransactionViewHolder holder;
        if(convertView != null){
            holder = (TransactionViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_transaction, parent, false);
            holder = new TransactionViewHolder(convertView);
            convertView.setTag(holder);
        }

        try {
            holder._date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(transaction.date));
        }
        catch(Exception x){
            holder._date.setText("Not Available");
        }
        DecimalFormat df = new DecimalFormat("#.00");
        holder._amount.setText("$ " + df.format(transaction.amount));
        holder._detail.setText(transaction.description);
        return convertView;
    }

    public class TransactionViewHolder {

        @Bind(R.id.date)
        TextView _date;
        @Bind(R.id.detail)
        TextView _detail;
        @Bind(R.id.amount)
        TextView _amount;

        public TransactionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
