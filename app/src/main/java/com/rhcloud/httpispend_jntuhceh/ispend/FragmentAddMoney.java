package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import java.util.Date;


public class FragmentAddMoney extends Fragment {

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

    Button buttonAddMoney;

    NavigationView navigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View addMoneyFragmentView  = inflater.inflate(R.layout.fragment_add_money, container, false);
        addMoneyFragmentView.setBackgroundColor(Color.WHITE);

        userLocalStore = new UserLocalStore(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        dateTimeHelper = new DateTimeHelper();

        Date today = new Date();
        displayDateString = dateTimeHelper.getDisplayStringFromDateObject(today);
        insertDateString = dateTimeHelper.getInsertString(today);

        editTextCategory = (EditText) addMoneyFragmentView.findViewById(R.id.editTextCategory);
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
        editTextDate = (EditText) addMoneyFragmentView.findViewById(R.id.editTextDate);
        editTextDate.setText(displayDateString);
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


        editTextAmount = (EditText) addMoneyFragmentView.findViewById(R.id.editTextAmount);
        editTextDescription = (EditText) addMoneyFragmentView.findViewById(R.id.editTextDescription);

        buttonAddMoney = (Button) addMoneyFragmentView.findViewById(R.id.buttonSpendMoney);
        buttonAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    String email = userLocalStore.getLoggedInUser().email;
                    String transactionType = "Income";
                    String transactionDate = insertDateString;
                    String transactionCategory = editTextCategory.getText().toString();
                    String transactionAmount = editTextAmount.getText().toString();
                    String transactionDescription = editTextDescription.getText().toString();

                    Transaction transaction = new Transaction(email, transactionAmount, transactionCategory, transactionDate, transactionDescription, transactionType);
                    databaseHelper.addMoney(transaction);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new FragmentTransactions()).commit();
                    navigationView = (NavigationView) ((View) (getActivity().findViewById(R.id.drawer_layout))).findViewById(R.id.navigationView);
                    navigationView.setCheckedItem(R.id.id_transactions);
                }
            }
        });

        return addMoneyFragmentView;
    }

    public void showListViewPopUp() {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Select Category");

        categoriesArrayList = databaseHelper.getCategoriesArrayList(userLocalStore.getLoggedInUser().email, "Income");
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

    public boolean validate() {

        String amount = editTextAmount.getText().toString();
        if(!isValidAmount(amount)) {
            editTextAmount.setError("Enter amount");
            editTextAmount.requestFocus();
            return false;
        }

        String date = editTextDate.getText().toString();
        if(!isValidDate(date)) {
            editTextDate.setError("Enter Date");
            editTextDate.requestFocus();
            return false;
        }

        String category = editTextCategory.getText().toString();
        if(!isValidCategory(category)) {
            editTextCategory.setError("Select a Category");
            editTextCategory.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidAmount(String amount) {
        if (amount != null && amount.length() > 0) {
            try {
                Float.parseFloat(amount);
                return true;
            }catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean isValidDate(String date) {
        if (date != null && date.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean isValidCategory(String category) {
        if (category != null && category.length() > 0) {
            return true;
        }
        return false;
    }

}
