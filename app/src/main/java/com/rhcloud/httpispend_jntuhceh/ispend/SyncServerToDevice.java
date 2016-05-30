package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by Muneer on 25-05-2016.
 */
public class SyncServerToDevice {

    Context context;
    UserLocalStore userLocalStore;
    DatabaseHelper databaseHelper;
    DateTimeHelper dateTimeHelper;
    HelperClass helperClass;

    SyncServerToDevice(Context context) {
        this.context = context;
        userLocalStore = new UserLocalStore(context);
        databaseHelper = new DatabaseHelper(context);
        dateTimeHelper = new DateTimeHelper();
        helperClass = new HelperClass(context);
    }

    void syncCategories() {
        Toast.makeText(context, "Synchronisation started", Toast.LENGTH_SHORT).show();
        if(helperClass.isNetworkAvailable()) {
            SyncServerToDeviceServerRequests syncServerToDeviceServerRequests = new SyncServerToDeviceServerRequests(context);
            syncServerToDeviceServerRequests.fetchCategoriesInBackground(userLocalStore.getLoggedInUser().email, new GetObjectCallback() {
                @Override
                public void done(Object returnedObject) {
                    databaseHelper.insertCategoriesFromServer(returnedObject.toString());
                    syncTransactions();
                }
            });
        }
        else {
            Toast.makeText(context, "No Internet connection detected - to Sync Categories from Server", Toast.LENGTH_LONG).show();
        }
    }

    void syncTransactions() {
        if(helperClass.isNetworkAvailable()) {
            SyncServerToDeviceServerRequests syncServerToDeviceServerRequests = new SyncServerToDeviceServerRequests(context);
            syncServerToDeviceServerRequests.fetchTransactionsInBackground(userLocalStore.getLoggedInUser().email, new GetObjectCallback() {
                @Override
                public void done(Object returnedObject) {
                    databaseHelper.insertTransactionsFromServer(returnedObject.toString());
                    Toast.makeText(context, "Synchronisation completed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(context, "No Internet connection detected - to Sync Transactions from Server", Toast.LENGTH_LONG).show();
            return;
        }
    }
}
