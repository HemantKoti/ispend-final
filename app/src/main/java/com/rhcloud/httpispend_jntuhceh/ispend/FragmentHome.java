package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentHome extends Fragment {

    private ActivityWelcome myContext1;

    TextView textViewDuration, textViewSpends, textViewTotalBudget, textViewAvailable;

    Button buttonAddMoney, buttonSpendMoney;

    UserLocalStore userLocalStore;
    DateTimeHelper dateTimeHelper;
    DatabaseHelper databaseHelper;

    View homeFragmentView;

    SpendsSummary spendsSummary;

    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    @Override
    public void onAttach(Activity activity) {
        myContext1=(ActivityWelcome) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        homeFragmentView.setBackgroundColor(Color.WHITE);

        userLocalStore = new UserLocalStore(getContext());
        dateTimeHelper = new DateTimeHelper();
        databaseHelper = new DatabaseHelper(getContext());

        textViewDuration = (TextView) homeFragmentView.findViewById(R.id.textViewDuration);

        textViewTotalBudget = (TextView) homeFragmentView.findViewById(R.id.textViewTotalBudget);
        textViewSpends = (TextView) homeFragmentView.findViewById(R.id.textViewSpends);
        textViewAvailable = (TextView) homeFragmentView.findViewById(R.id.textViewAvailable);

        buttonAddMoney = (Button) homeFragmentView.findViewById(R.id.buttonAddMoney);
        buttonAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new FragmentAddMoney());
                fragmentTransaction.commit();
                navigationView = (NavigationView) getActivity().findViewById(R.id.navigationView);
                navigationView.setCheckedItem(R.id.id_add_money);
            }
        });

        buttonSpendMoney = (Button) homeFragmentView.findViewById(R.id.buttonSpendMoney);
        buttonSpendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new FragmentSpendMoney());
                fragmentTransaction.commit();
                navigationView = (NavigationView) getActivity().findViewById(R.id.navigationView);
                navigationView.setCheckedItem(R.id.id_spend_money);
            }
        });

        updateContents();

        return homeFragmentView;
    }

    public void updateContents() {
        textViewDuration.setText(dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()));

        spendsSummary = databaseHelper.getTransactionSummary();

        textViewTotalBudget.setText("₹" + spendsSummary.totalBudget);
        textViewSpends.setText("₹" + spendsSummary.totalSpends);
        textViewAvailable.setText("₹" + spendsSummary.availableAmount);
    }
}
