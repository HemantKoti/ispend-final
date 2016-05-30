package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import java.util.HashMap;

public class ActivityWelcome extends AppCompatActivity {

    ProgressDialog progressDialog;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    UserLocalStore userLocalStore;
    User user;
    TextView navigationDrawerHeaderName, navigationDrawerHeaderEmail;

    View navigationHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        progressDialog = new ProgressDialog(ActivityWelcome.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");

        userLocalStore = new UserLocalStore(this);
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        user = userLocalStore.getLoggedInUser();


        DateRange dateRange = new DateRange("m");
        userLocalStore.setDateRange(dateRange);
        userLocalStore.setDurationType("m");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, new FragmentHome());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setCheckedItem(R.id.id_home);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_home:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentHome());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_add_money:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentAddMoney());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_spend_money:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentSpendMoney());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_transactions:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentTransactions());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_categories:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentCategories());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_bar_graph:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentBarGraph());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_pie_chart:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentPieChart());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_synchronize:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentSynchronize());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_offers:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContainer, new FragmentOffers());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(new DateTimeHelper().getActionBarDisplayString(userLocalStore.getDateRange()));
                        drawerLayout.closeDrawers();
                        item.setChecked(true);
                        break;

                    case R.id.id_logout:
                        userLocalStore.clearUserData();
                        userLocalStore.setUserLoggedIn(false);
                        Intent intent = new Intent(ActivityWelcome.this, ActivityLogin.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

        navigationHeader = navigationView.getHeaderView(0);
        navigationDrawerHeaderName = (TextView) navigationHeader.findViewById(R.id.navigationDrawerHeaderName);
        navigationDrawerHeaderEmail = (TextView) navigationHeader.findViewById(R.id.navigationDrawerHeaderEmail);
        navigationDrawerHeaderName.setText(user.name);
        navigationDrawerHeaderEmail.setText(user.email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DateRange dateRange = userLocalStore.getDateRange();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        switch (item.getItemId()) {
            case R.id.id_previous:
                switch (userLocalStore.getDurationType()) {
                    case "w":
                        dateRange.subtractWeek();
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivityWelcome.this, "week subtracted", Toast.LENGTH_SHORT).show();
                        break;
                    case "m":
                        dateRange.subtractMonth();
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivityWelcome.this, "month subtracted", Toast.LENGTH_SHORT).show();
                        break;
                    case "y":
                        dateRange.subtractYear();
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivityWelcome.this, "year subtracted", Toast.LENGTH_SHORT).show();
                        break;
                }
                updateCurrentFragment();
                break;
            case R.id.id_next:
                switch (userLocalStore.getDurationType()) {
                    case "w":
                        dateRange.addWeek();
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivityWelcome.this, "week added", Toast.LENGTH_SHORT).show();
                        break;
                    case "m":
                        dateRange.addMonth();
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivityWelcome.this, "month added", Toast.LENGTH_SHORT).show();
                        break;
                    case "y":
                        dateRange.addYear();
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ActivityWelcome.this, "year added", Toast.LENGTH_SHORT).show();
                        break;
                }
                updateCurrentFragment();
                break;
            case R.id.id_duration:
                View menuItemView = findViewById(R.id.id_duration);
                showPopUpMenu(findViewById(R.id.id_duration));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainContainer);
        if(currentFragment instanceof FragmentHome) {
            //Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
            ((FragmentHome) currentFragment).updateContents();
        }
        else if(currentFragment instanceof FragmentTransactions) {
            //Toast.makeText(getApplicationContext(), "Transactions", Toast.LENGTH_SHORT).show();
            ((FragmentTransactions) currentFragment).updateContents();
        }
        else if(currentFragment instanceof FragmentPieChart) {
            ((FragmentPieChart) currentFragment).updateContents();
        }
        else if(currentFragment instanceof FragmentBarGraph) {
            ((FragmentBarGraph) currentFragment).updateContents();
        }
    }

    public void showPopUpMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DateRange dateRange;
                DateTimeHelper dateTimeHelper = new DateTimeHelper();
                switch (item.getItemId()) {
                    case R.id.id_weekly:
                        userLocalStore.setDurationType("w");
                        dateRange = new DateRange("w");
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.id_monthly:
                        userLocalStore.setDurationType("m");
                        dateRange = new DateRange("m");
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.id_yearly:
                        userLocalStore.setDurationType("y");
                        dateRange = new DateRange("y");
                        userLocalStore.setDateRange(dateRange);
                        (ActivityWelcome.this).getSupportActionBar().setTitle(dateTimeHelper.getActionBarDisplayString(dateRange));
                        //Toast.makeText(ActivityWelcome.this, dateTimeHelper.getFullDisplayString(userLocalStore.getDateRange()), Toast.LENGTH_SHORT).show();
                        break;
                }
                updateCurrentFragment();
                return false;
            }
        });
    }
}
