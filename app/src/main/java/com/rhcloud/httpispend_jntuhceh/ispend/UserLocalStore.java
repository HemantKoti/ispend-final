package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Muneer on 09-03-2016.
 */
public class UserLocalStore
{
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;
    Context context;

    public UserLocalStore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("email", user.email);
        userLocalDatabaseEditor.putString("mobile", user.mobile);
        userLocalDatabaseEditor.putString("name", user.name);
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData()
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser()
    {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false)
        {
            return null;
        }

        String email = userLocalDatabase.getString("email", "");
        String mobile = userLocalDatabase.getString("mobile", "");
        String name = userLocalDatabase.getString("name", "");
        String password = userLocalDatabase.getString("password", "");

        User user = new User(email, mobile, name, password);
        return user;
    }

    public DateRange getDateRange() {

        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        DateRange dateRange = new DateRange("m");

        String startDateInsertString = userLocalDatabase.getString("startDate", dateTimeHelper.getInsertString(dateRange.startDateObject));
        String endDateInsertInsertString = userLocalDatabase.getString("endDate", dateTimeHelper.getInsertString(dateRange.endDateObject));

        dateRange.startDateObject = dateTimeHelper.getStartDateObjectFromInsertString(startDateInsertString);
        dateRange.endDateObject = dateTimeHelper.getEndDateObjectFromInsertString(endDateInsertInsertString);

        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        userLocalDatabaseEditor.putString("startDate", dateTimeHelper.getInsertString(dateRange.startDateObject));
        userLocalDatabaseEditor.putString("endDate", dateTimeHelper.getInsertString(dateRange.endDateObject));
        userLocalDatabaseEditor.commit();
    }

    public String getActionBarDisplayString() {
        DateTimeHelper dateTimeHelper = new DateTimeHelper();
        DateRange dateRange = new DateRange("m");
        String actionBarDisplayString = userLocalDatabase.getString("actionBarDisplayString", dateTimeHelper.getActionBarDisplayString(dateRange));
        return actionBarDisplayString;
    }

    public void setActionBarDisplayString(String actionBarDisplayString) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("actionBarDisplayString", actionBarDisplayString);
        userLocalDatabaseEditor.commit();
    }

    public String getDurationType() {
        String durationType = userLocalDatabase.getString("durationType", "m");
        return durationType;
    }

    public void setDurationType(String durationType) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("durationType", durationType);
        userLocalDatabaseEditor.commit();
    }
}

