package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentCategories extends Fragment {

    ListView listViewCategories;
    ArrayAdapter<String> listViewCategoriesArrayAdapter;

    String[] categoriesArray;
    ArrayList<String> categoriesArrayList;

    DatabaseHelper databaseHelper;
    UserLocalStore userLocalStore;

    Button buttonAddCategory;
    RadioGroup radioGroupCategoryType;
    RadioButton radioButton;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View categoriesFragmentView  = inflater.inflate(R.layout.fragment_categories, container, false);
        categoriesFragmentView.setBackgroundColor(Color.WHITE);

        userLocalStore = new UserLocalStore(getContext());
        databaseHelper = new DatabaseHelper(getContext());

        listViewCategories = (ListView) categoriesFragmentView.findViewById(R.id.listViewCategories);

        radioGroupCategoryType = (RadioGroup) categoriesFragmentView.findViewById(R.id.radioGroupCategoryType);
        radioGroupCategoryType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedIndex = radioGroupCategoryType.getCheckedRadioButtonId();
                radioButton = (RadioButton) categoriesFragmentView.findViewById(selectedIndex);
//                Toast.makeText(getContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                updateListViewCategories(radioButton.getText().toString());
            }
        });


        final RadioButton radioButtonSpends = (RadioButton) categoriesFragmentView.findViewById(R.id.radioButtonSpends);
        radioGroupCategoryType.check(radioButtonSpends.getId());

        listViewCategories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedRadioButtonID = radioGroupCategoryType.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) categoriesFragmentView.findViewById(selectedRadioButtonID);
                String categoryType = selectedRadioButton.getText().toString();

                showRemoveCategoryAlertDialog(parent.getItemAtPosition(position).toString(), categoryType);
                return false;
            }
        });

        buttonAddCategory = (Button) categoriesFragmentView.findViewById(R.id.buttonAddCategory);
        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioGroupCategoryType.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) categoriesFragmentView.findViewById(radioButtonId);
                showAddCategoryAlertDialog(selectedRadioButton.getText().toString());
            }
        });

        return categoriesFragmentView;
    }

    void showRemoveCategoryAlertDialog(final String categoryName, final String categoryType) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Delete " + categoryName + " and Transactions with Category " + categoryName + "?");

        alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                databaseHelper.deleteCategory(userLocalStore.getLoggedInUser().email, categoryName, categoryType);
                updateListViewCategories(categoryType);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
                        updateListViewCategories(categoryType);
                    }
                });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    void updateListViewCategories(String categoryType) {
        categoriesArrayList = databaseHelper.getCategoriesArrayList(userLocalStore.getLoggedInUser().email, categoryType);
        categoriesArray = new String[categoriesArrayList.size()];
        categoriesArray = categoriesArrayList.toArray(categoriesArray);

        listViewCategoriesArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categoriesArray);
        listViewCategories.setAdapter(listViewCategoriesArrayAdapter);

    }
}
