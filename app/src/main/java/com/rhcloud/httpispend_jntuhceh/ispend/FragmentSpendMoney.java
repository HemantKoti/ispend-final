package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentSpendMoney extends Fragment {

    ListView listViewCategories;
    ArrayAdapter<String> listViewCategoriesArrayAdapter;

    String[] categoriesArray;
    ArrayList<String> categoriesArrayList;

    DatabaseHelper databaseHelper;
    UserLocalStore userLocalStore;
    DateTimeHelper dateTimeHelper;

    Calendar myCalendar;
    String insertDateString, displayDateString;
    Integer year_x, month_x, day_x;

    EditText editTextCategory;
    EditText editTextAmount;
    EditText editTextDate;
    EditText editTextDescription;

    Button buttonSpendMoney;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View spendMoneyFragmentView  = inflater.inflate(R.layout.fragment_spend_money, container, false);
        spendMoneyFragmentView.setBackgroundColor(Color.WHITE);

        userLocalStore = new UserLocalStore(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        dateTimeHelper = new DateTimeHelper();

        editTextCategory = (EditText) spendMoneyFragmentView.findViewById(R.id.editTextCategory);
        editTextCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    showListViewPopUp();
                }
                return true;
            }
        });

        myCalendar = Calendar.getInstance();
        editTextDate = (EditText) spendMoneyFragmentView.findViewById(R.id.editTextDate);
        editTextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    new DatePickerDialog(getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });


        editTextAmount = (EditText) spendMoneyFragmentView.findViewById(R.id.editTextAmount);
        editTextDescription = (EditText) spendMoneyFragmentView.findViewById(R.id.editTextDescription);

        buttonSpendMoney = (Button) spendMoneyFragmentView.findViewById(R.id.buttonSpendMoney);
        buttonSpendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userLocalStore.getLoggedInUser().email;
                String transactionType = "Spends";
                String transactionDate = insertDateString;
                String transactionCategory = editTextCategory.getText().toString();
                String transactionAmount = editTextAmount.getText().toString();
                String transactionDescription = editTextDescription.getText().toString();

                Transaction transaction = new Transaction(email, transactionAmount, transactionCategory, transactionDate, transactionDescription, transactionType);
                databaseHelper.spendMoney(transaction);
            }
        });

        return spendMoneyFragmentView;
    }

    public void showListViewPopUp() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Select Category");

        categoriesArrayList = databaseHelper.getCategoriesArrayList(userLocalStore.getLoggedInUser().email, "Spends");
        categoriesArray = new String[categoriesArrayList.size()];
        categoriesArray = categoriesArrayList.toArray(categoriesArray);
        listViewCategoriesArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice, categoriesArray);

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setPositiveButton("Add New Category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAddCategoryAlertDialog("Income");
            }
        });

        builderSingle.setAdapter(listViewCategoriesArrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = listViewCategoriesArrayAdapter.getItem(which);
                editTextCategory.setText(category);
                //Toast.makeText(getContext(), category, Toast.LENGTH_SHORT).show();
            }
        });

        builderSingle.show();
    }

    void showAddCategoryAlertDialog(final String categoryType) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Enter Category Name");

        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String categoryName = input.getText().toString();
//                        Toast.makeText(getContext(), categoryName, Toast.LENGTH_SHORT).show();
                        databaseHelper.addCategory(userLocalStore.getLoggedInUser().email, categoryName, categoryType);
                        showListViewPopUp();
                    }
                });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;

            insertDateString = dateTimeHelper.getInsertDateStringFromTheseIntegers(year_x, month_x, day_x);
            displayDateString = dateTimeHelper.getDisplayDateStringFromInsertDateString(insertDateString);

            editTextDate.setText(displayDateString);
            Toast.makeText(getContext(), insertDateString, Toast.LENGTH_SHORT).show();
        }
    };
}
