package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muneer on 06-03-2016.
 */
public class TransactionAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public TransactionAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Transaction transaction) {
        super.add(transaction);
        list.add(transaction);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TransactionHolder transactionHolder;
        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.transaction_layout, parent, false);
            transactionHolder = new TransactionHolder();
            transactionHolder.textViewCategory = (TextView) row.findViewById(R.id.textViewCategorySS);
            transactionHolder.textViewDate = (TextView) row.findViewById(R.id.textViewDate);
            transactionHolder.textViewAmount = (TextView) row.findViewById(R.id.textViewAmount);
            transactionHolder.textViewDescription = (TextView) row.findViewById(R.id.textViewDescription);
            row.setTag(transactionHolder);
        }
        else
        {
            transactionHolder = (TransactionHolder) row.getTag();
        }

        Transaction transaction = (Transaction) this.getItem(position);
        transactionHolder.textViewCategory.setText(transaction.getTransactionCategory());
        transactionHolder.textViewDate.setText(transaction.getTransactionDate());
        transactionHolder.textViewAmount.setText("₹" + transaction.getTransactionAmount());
        transactionHolder.textViewDescription.setText(transaction.getTransactionDescription());


       if(transaction.getTransactionType().equals("Spends")) {
           transactionHolder.textViewAmount.setTextColor(Color.parseColor("#ff0000"));
       }
        else {
           transactionHolder.textViewAmount.setTextColor(Color.parseColor("#689f38"));
       }

        return row;
    }

    static class TransactionHolder {
        TextView textViewCategory, textViewDate, textViewAmount, textViewDescription;
    }
}
