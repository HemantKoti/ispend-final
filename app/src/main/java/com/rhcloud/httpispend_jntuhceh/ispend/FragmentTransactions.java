package com.rhcloud.httpispend_jntuhceh.ispend;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class FragmentTransactions extends Fragment {

    TextView textViewDuration;

    ListView listViewTransactions;

    RadioGroup radioGroupTransactionsType;

    RadioButton radioButtonAll;
    RadioButton radioButtonSpends;
    RadioButton radioButtonIncome;
    RadioButton radioButtonSummary;

    UserLocalStore userLocalStore;
    DatabaseHelper databaseHelper;
    DateTimeHelper dateTimeHelper;

    Cursor cursor;

    View fragmentTransactionsView;

    String radioButtonTransactionTypeString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTransactionsView = inflater.inflate(R.layout.fragment_transactions, container, false);
        fragmentTransactionsView.setBackgroundColor(Color.WHITE);

        userLocalStore = new UserLocalStore(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        dateTimeHelper = new DateTimeHelper();

        textViewDuration = (TextView) fragmentTransactionsView.findViewById(R.id.textViewDuration);

        listViewTransactions = (ListView) fragmentTransactionsView.findViewById(R.id.listViewTransactions);

        radioGroupTransactionsType = (RadioGroup) fragmentTransactionsView.findViewById(R.id.radioGroupTransactionsType);

        radioButtonAll = (RadioButton) fragmentTransactionsView.findViewById(R.id.radioButtonAll);
        radioButtonSpends = (RadioButton) fragmentTransactionsView.findViewById(R.id.radioButtonSpends);
        radioButtonIncome = (RadioButton) fragmentTransactionsView.findViewById(R.id.radioButtonIncome);
        radioButtonSummary = (RadioButton) fragmentTransactionsView.findViewById(R.id.radioButtonSummary);

        radioGroupTransactionsType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateContents();
            }
        });

        radioButtonAll.setChecked(true);

        return fragmentTransactionsView;
    }

    public void updateContents() {
        textViewDuration.setText(dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()));

        int checkedRadioButtonId = radioGroupTransactionsType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) fragmentTransactionsView.findViewById(checkedRadioButtonId);
        radioButtonTransactionTypeString = selectedRadioButton.getText().toString();

        cursor = databaseHelper.getTransactions();

        switch (radioButtonTransactionTypeString) {
            case "All":
                showAllTransactions();
                break;
            case "Spends":
                showSpendsTransactions();
                break;
            case "Income":
                showIncomeTransactions();
                break;
            case "Summary":
                showTransactionsSummary();
                break;
        }
        //Toast.makeText(getContext(), "ListView contents updated", Toast.LENGTH_SHORT).show();
    }

    public void showAllTransactions() {
        try {
            TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), R.layout.transaction_layout);
            while(cursor.moveToNext()) {
                String transactionCategory = cursor.getString(cursor.getColumnIndex("TransactionCategory"));
                String transactionDate = dateTimeHelper.getFullDisplayString(dateTimeHelper.getStartDateObjectFromInsertString(cursor.getString(cursor.getColumnIndex("TransactionDate"))));
                String transactionAmount = cursor.getString(cursor.getColumnIndex("TransactionAmount"));
                String transactionDescription = cursor.getString(cursor.getColumnIndex("TransactionDescription"));
                String transactionType = cursor.getString(cursor.getColumnIndex("TransactionType"));

                Transaction transaction = new Transaction(userLocalStore.getLoggedInUser().email, transactionAmount, transactionCategory, transactionDate, transactionDescription, transactionType);
                transactionAdapter.add(transaction);
            }
            listViewTransactions.setAdapter(transactionAdapter);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void showSpendsTransactions() {
        try {
            TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), R.layout.transaction_layout);
            while(cursor.moveToNext()) {
                String transactionCategory = cursor.getString(cursor.getColumnIndex("TransactionCategory"));
                String transactionDate = dateTimeHelper.getFullDisplayString(dateTimeHelper.getStartDateObjectFromInsertString(cursor.getString(cursor.getColumnIndex("TransactionDate"))));
                String transactionAmount = cursor.getString(cursor.getColumnIndex("TransactionAmount"));
                String transactionDescription = cursor.getString(cursor.getColumnIndex("TransactionDescription"));
                String transactionType = cursor.getString(cursor.getColumnIndex("TransactionType"));

                if(transactionType.equals("Spends")) {
                    Transaction transaction = new Transaction(userLocalStore.getLoggedInUser().email, transactionAmount, transactionCategory, transactionDate, transactionDescription, transactionType);
                    transactionAdapter.add(transaction);
                }
            }
            listViewTransactions.setAdapter(transactionAdapter);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void showIncomeTransactions() {
        try {
            TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), R.layout.transaction_layout);
            while(cursor.moveToNext()) {
                String transactionCategory = cursor.getString(cursor.getColumnIndex("TransactionCategory"));
                String transactionDate = dateTimeHelper.getFullDisplayString(dateTimeHelper.getStartDateObjectFromInsertString(cursor.getString(cursor.getColumnIndex("TransactionDate"))));
                String transactionAmount = cursor.getString(cursor.getColumnIndex("TransactionAmount"));
                String transactionDescription = cursor.getString(cursor.getColumnIndex("TransactionDescription"));
                String transactionType = cursor.getString(cursor.getColumnIndex("TransactionType"));

                if(transactionType.equals("Income")) {
                    Transaction transaction = new Transaction(userLocalStore.getLoggedInUser().email, transactionAmount, transactionCategory, transactionDate, transactionDescription, transactionType);
                    transactionAdapter.add(transaction);
                }
            }
            listViewTransactions.setAdapter(transactionAdapter);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void showTransactionsSummary() {
//        Toast.makeText(getContext(), "entered", Toast.LENGTH_SHORT).show();
        try {
            TransactionSummaryAdapter transactionSummaryAdapter = new TransactionSummaryAdapter(getContext(), R.layout.transaction_summary_layout);

            HashMap<String, String> transactionsSummaryHashMap = databaseHelper.getTransactionSummaryHashMap();

            if(transactionsSummaryHashMap == null || transactionsSummaryHashMap.size() == 0) {
                //do nothing
            }
            else {
                for(String category : transactionsSummaryHashMap.keySet()) {
                    TransactionSummary transactionSummary = new TransactionSummary(category, transactionsSummaryHashMap.get(category));
                    transactionSummaryAdapter.add(transactionSummary);
                }
            }
            listViewTransactions.setAdapter(transactionSummaryAdapter);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
