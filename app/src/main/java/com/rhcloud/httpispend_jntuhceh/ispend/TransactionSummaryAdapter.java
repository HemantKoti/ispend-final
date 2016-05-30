package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
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
public class TransactionSummaryAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public TransactionSummaryAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(TransactionSummary object) {
        super.add(object);
        list.add(object);
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
        ItemHolder itemHolder;

        if(row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.transaction_summary_layout, parent, false);
            itemHolder = new ItemHolder();
            itemHolder.textViewCategory = (TextView) row.findViewById(R.id.textViewCategory);
            itemHolder.textViewTotalAmount = (TextView) row.findViewById(R.id.textViewTotalAmount);
            row.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemHolder) row.getTag();
        }

        TransactionSummary transactionSummary = (TransactionSummary) this.getItem(position);
        itemHolder.textViewCategory.setText(transactionSummary.getTransactionCategory());
        itemHolder.textViewTotalAmount.setText("₹" + transactionSummary.getTotalAmount());

        return row;
    }

    static class ItemHolder {
        TextView textViewCategory, textViewTotalAmount;
    }
}
