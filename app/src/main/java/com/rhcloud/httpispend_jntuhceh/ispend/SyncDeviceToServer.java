package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Muneer on 24-05-2016.
 */
public class SyncDeviceToServer {

    Context context;
    UserLocalStore userLocalStore;
    DatabaseHelper databaseHelper;
    DateTimeHelper dateTimeHelper;
    HelperClass helperClass;

    SyncDeviceToServer(Context context) {
        this.context = context;
        userLocalStore = new UserLocalStore(context);
        databaseHelper = new DatabaseHelper(context);
        dateTimeHelper = new DateTimeHelper();
        helperClass = new HelperClass(context);
    }

    void syncUser() {
        Toast.makeText(context, "Synchronisation started", Toast.LENGTH_SHORT).show();
        Cursor cursor = databaseHelper.getDirtyUser();

        if(cursor == null || cursor.getCount() == 0) {
            Toast.makeText(context, "Sync User not required", Toast.LENGTH_SHORT).show();
            syncCategories();
        }
        else {
            if(helperClass.isNetworkAvailable()) {
                SyncDeviceToServerServerRequests syncDeviceToServerServerRequests = new SyncDeviceToServerServerRequests(context);
                syncDeviceToServerServerRequests.syncUserInBackground(userLocalStore.getLoggedInUser(), new GetObjectCallback() {
                    @Override
                    public void done(Object returnedObject) {
                        if (returnedObject != null) {
                            String status = returnedObject.toString();
                            if (status.equals("Registration Successfull")) {
                                databaseHelper.makeUserNotDirty();
                                Toast.makeText(context, "Sync User completed successfully", Toast.LENGTH_SHORT).show();
                                syncCategories();
                            } else {
                                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
                                syncCategories();
                            }
                        } else {
                            Toast.makeText(context, "Unable to Sync User to server...try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(context, "No Internet connection detected - to Sync User to Server", Toast.LENGTH_LONG).show();
            }
        }
    }

    void syncCategories() {

        Cursor cursor = databaseHelper.getDirtyCategories();
        if(cursor == null || cursor.getCount() == 0) {
            Toast.makeText(context, "Sync Categories not required", Toast.LENGTH_SHORT).show();
            syncTransactions();
        }
        else {
            String json_string ="{\"categories\":[";

            final int noOfDirtyPurchases = cursor.getCount();
            int i = 1;
            while(cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Email", cursor.getString(0));
                    jsonObject.put("CategoryName", cursor.getString(1));
                    jsonObject.put("CategoryType", cursor.getString(2));

                    if(i != noOfDirtyPurchases)
                        json_string = json_string + jsonObject.toString() + ",";
                    else
                        json_string = json_string + jsonObject.toString() + "}";

                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Close JSON string
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";

            if(helperClass.isNetworkAvailable()) {
                SyncDeviceToServerServerRequests syncDeviceToServerServerRequests = new SyncDeviceToServerServerRequests(context);
                syncDeviceToServerServerRequests.syncCategoriesInBackGround(userLocalStore.getLoggedInUser().email, json_string, new GetObjectCallback() {
                    @Override
                    public void done(Object returnedObject) {
                        if(returnedObject != null) {
                            String status = returnedObject.toString();
                            if(status.contains("OK")) {
                                databaseHelper.makeCategoriesNotDirty();
                                if(noOfDirtyPurchases == 1) {
                                    Toast.makeText(context, noOfDirtyPurchases + " Category synced to server", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, noOfDirtyPurchases + " Categories synced to server", Toast.LENGTH_SHORT).show();
                                }
                                syncTransactions();
                            }
                            else {
                                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
                                syncTransactions();
                            }
                        }
                        else {
                            Toast.makeText(context, "Unable to Sync Categories to server...try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(context, "No Internet connection detected - to Sync Categories to Server", Toast.LENGTH_LONG).show();
            }
        }
    }

    void syncTransactions() {
        Cursor cursor = databaseHelper.getDirtyTransactions();
        if(cursor == null || cursor.getCount() == 0) {
            Toast.makeText(context, "Sync Transactions not required", Toast.LENGTH_SHORT).show();
        }
        else {
            String json_string ="{\"transactions\":[";

            final int noOfDirtyPurchases = cursor.getCount();
            int i = 1;
            while(cursor.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Email", cursor.getString(1));
                    jsonObject.put("TransactionType", cursor.getString(2));
                    jsonObject.put("TransactionDate", cursor.getString(3));
                    jsonObject.put("TransactionCategory", cursor.getString(4));
                    jsonObject.put("TransactionAmount", cursor.getString(5));
                    jsonObject.put("TransactionDescription", cursor.getString(6));

                    if(i != noOfDirtyPurchases)
                        json_string = json_string + jsonObject.toString() + ",";
                    else
                        json_string = json_string + jsonObject.toString() + "}";

                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Close JSON string
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";

            if(helperClass.isNetworkAvailable()) {
                SyncDeviceToServerServerRequests syncDeviceToServerServerRequests = new SyncDeviceToServerServerRequests(context);
                syncDeviceToServerServerRequests.syncTransactionsInBackGround(userLocalStore.getLoggedInUser().email, json_string, new GetObjectCallback() {
                    @Override
                    public void done(Object returnedObject) {
                        if (returnedObject != null) {
                            String status = returnedObject.toString();
                            if (status.contains("OK")) {
                                databaseHelper.makeTransactionsNotDirty();
                                if(noOfDirtyPurchases == 1) {
                                    Toast.makeText(context, noOfDirtyPurchases + " Transaction synced to server", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, noOfDirtyPurchases + " Transactions synced to server", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Unable to Sync Transactions to server...try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(context, "No Internet connection detected - to Sync Transactions to Server", Toast.LENGTH_LONG).show();
            }
        }
        Toast.makeText(context, "Synchronisation completed", Toast.LENGTH_SHORT).show();
    }
}