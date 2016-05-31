package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Muneer on 23-05-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "local.db";

    static final String USERS_TABLE_NAME = "Users";
    static final String TRANSACTIONS_TABLE_NAME = "Transactions";
    static final String CATEGORIES_TABLE_NAME = "Categories";

    static final String CREATE_USERS_TABLE_QUERY = "CREATE TABLE Users (Email varchar(100) PRIMARY KEY, Mobile varchar(20) UNIQUE, Name varchar(100), Password varchar(50), IsDirty INTEGER DEFAULT 1)";
    static final String CREATE_TRANSACTIONS_TABLE_QUERY = "CREATE TABLE Transactions (TransactionID INTEGER PRIMARY KEY AUTOINCREMENT, Email varchar(100), TransactionType varchar(20) DEFAULT NULL, TransactionDate DATE DEFAULT NULL, TransactionCategory varchar(50) DEFAULT NULL, TransactionAmount INTEGER DEFAULT 0, TransactionDescription varchar(100), IsDirty INTEGER DEFAULT 1, FOREIGN KEY (Email) REFERENCES Users(Email), CONSTRAINT TransactionsConstraint UNIQUE(Email, TransactionType,  TransactionDate, TransactionCategory, TransactionAmount, TransactionDescription))";
    static final String CREATE_CATEGORIES_TABLE_QUERY = "CREATE TABLE Categories (Email varchar(100) NOT NULL, CategoryName varchar(50) NOT NULL, CategoryType varchar(20) NOT NULL DEFAULT 'Spends', IsDirty INTEGER DEFAULT 1, CONSTRAINT CategoriesConstraint UNIQUE(Email, CategoryName, CategoryType))";

    private final Context context;
    UserLocalStore userLocalStore;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        userLocalStore = new UserLocalStore(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE_QUERY);
        db.execSQL(CREATE_CATEGORIES_TABLE_QUERY);
        db.execSQL(CREATE_TRANSACTIONS_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME);
        onCreate(db);
    }

    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", user.email);
        contentValues.put("Mobile", user.mobile);
        contentValues.put("Name", user.name);
        contentValues.put("Password", user.password);
        long res = db.insert(USERS_TABLE_NAME, null, contentValues);
        if(res == -1)
            return false;
        else {
            String[] categoriesArray = {"Food", "Entertainment", "Electronics", "Clothing", "Footwear" , "Misc."};
            for(int i = 0; i < categoriesArray.length; i++) {
                contentValues = new ContentValues();
                contentValues.put("Email", user.email);
                contentValues.put("CategoryName", categoriesArray[i]);
                contentValues.put("CategoryType", "Spends");
                db.insert(CATEGORIES_TABLE_NAME, null, contentValues);
            }

            categoriesArray = new String[]{"Salary", "Pocket Money"};
            for(int i = 0; i < categoriesArray.length; i++) {
                contentValues = new ContentValues();
                contentValues.put("Email", user.email);
                contentValues.put("CategoryName", categoriesArray[i]);
                contentValues.put("CategoryType", "Income");
                db.insert(CATEGORIES_TABLE_NAME, null, contentValues);
            }

            return true;
        }
    }

    public void logUserIn(User returnedUser)
    {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        Intent intent = new Intent(context, ActivityWelcome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void validateLogin(User user) {
        Cursor res = null;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String loginQuery = "SELECT * FROM Users WHERE Email = '" + user.email +  "'";
            res = db.rawQuery(loginQuery, null);
            if(res == null || res.getCount() == 0) {
                Toast.makeText(context, "User not found on device...attempting to find User online", Toast.LENGTH_SHORT).show();
                if(new HelperClass(context).isNetworkAvailable()) {
                    getUserFromServer(user);
                }
                else {
                    Toast.makeText(context, "No Internet connection detected - to Sync User from Server", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                loginQuery = "SELECT * FROM Users WHERE Email = '" + user.email + "' AND Password = '" + user.password + "'";
                res = db.rawQuery(loginQuery, null);
                if(res == null || res.getCount() == 0) {
                    Toast.makeText(context, "Invalid Email / Password", Toast.LENGTH_LONG).show();
                }
                else {
                    StringBuffer buff = new StringBuffer();
                    if(res.moveToNext()) {
                        user.name = res.getString(2);
                        user.mobile = res.getString(1);
                        logUserIn(user);
                    }
                }
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<String> getCategoriesArrayList(String email, String categoryType) {
        Cursor res = null;
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            String loginQuery = "SELECT * FROM Categories WHERE Email = '" + email + "' AND CategoryType = '" + categoryType + "' ORDER BY CategoryName";
            res = db.rawQuery(loginQuery, null);
            if(res == null || res.getCount() == 0) {
                return arrayList;
            }
            else {
                while(res.moveToNext())
                    arrayList.add(res.getString(1));
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return arrayList;
    }

    public void addCategory(String email, String categoryName, String categoryType) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Email", email);
            contentValues.put("CategoryName", categoryName);
            contentValues.put("CategoryType", categoryType);
            long status = db.insert(CATEGORIES_TABLE_NAME, null, contentValues);
            if(status == -1) {
                Toast.makeText(context, "Unable to add category: " + categoryName, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, categoryName + " added successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteCategory(String email, String categoryName, String categoryType) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            if(db.delete(CATEGORIES_TABLE_NAME, "CategoryName = ? AND Email = ? AND CategoryType = ?", new String[] {categoryName, email, categoryType}) > 0) {
                Toast.makeText(context, categoryName + " deleted successfully", Toast.LENGTH_SHORT).show();
                int count = db.delete(TRANSACTIONS_TABLE_NAME, "Email = ? AND TransactionCategory = ?", new String[] {email, categoryName});
                if(count == 1) {
                    Toast.makeText(context, count + " Transaction with category " + categoryName + " deleted", Toast.LENGTH_SHORT).show();
                }
                else if(count > 0){
                    Toast.makeText(context, count + " Transactions with category " + categoryName + " deleted", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(context, "Unable delete " + categoryName, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void addMoney(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Email", transaction.email);
            contentValues.put("TransactionType", transaction.transactionType);
            contentValues.put("TransactionDate", transaction.transactionDate);
            contentValues.put("TransactionDate", transaction.transactionDate);
            contentValues.put("TransactionCategory", transaction.transactionCategory);
            contentValues.put("TransactionAmount", transaction.transactionAmount);
            contentValues.put("TransactionDescription", transaction.transactionDescription);
            contentValues.put("IsDirty", 1);
            long status = db.insert(TRANSACTIONS_TABLE_NAME, null, contentValues);
            if(status == -1) {
                Toast.makeText(context, "This transaction is already added", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, transaction.transactionAmount + " added successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void spendMoney(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Email", transaction.email);
            contentValues.put("TransactionType", transaction.transactionType);
            contentValues.put("TransactionDate", transaction.transactionDate);
            contentValues.put("TransactionDate", transaction.transactionDate);
            contentValues.put("TransactionCategory", transaction.transactionCategory);
            contentValues.put("TransactionAmount", transaction.transactionAmount);
            contentValues.put("TransactionDescription", transaction.transactionDescription);
            contentValues.put("IsDirty", 1);
            long status = db.insert(TRANSACTIONS_TABLE_NAME, null, contentValues);
            if(status == -1) {
                Toast.makeText(context, "This transaction is already added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, transaction.transactionAmount + " spent successfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public SpendsSummary getTransactionSummary() {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        DateRange dateRange = userLocalStore.getDateRange();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        String query;
        Integer totalBudget = 0, totalSpends = 0, availableAmount = 0;

        String email = userLocalStore.getLoggedInUser().email;
        String startDateString = dateTimeHelper.getInsertString(dateRange.startDateObject);
        String endDateString = dateTimeHelper.getInsertString(dateRange.endDateObject);
        try {
            query = "SELECT SUM(TransactionAmount) FROM Transactions WHERE Email = '" + email + "' AND TransactionType = 'Income' AND TransactionDate BETWEEN '" + startDateString + "' AND '" + endDateString + "'";
            cursor = db.rawQuery(query, null);
            if(cursor != null && cursor.getCount() > 0) {
                if(cursor.moveToNext())
                    totalBudget = cursor.getInt(0);
            }
            else {
                Toast.makeText(context, "Unable to retrieve total income", Toast.LENGTH_SHORT).show();
            }

            query = "SELECT SUM(TransactionAmount) FROM Transactions WHERE Email = '" + email + "' AND TransactionType = 'Spends' AND TransactionDate BETWEEN '" + startDateString + "' AND '" + endDateString+"'";
            cursor = db.rawQuery(query, null);
            if(cursor != null && cursor.getCount() > 0) {
                if(cursor.moveToNext())
                    totalSpends = cursor.getInt(0);
            }
            else {
                Toast.makeText(context, "Unable to retrieve total Spends", Toast.LENGTH_SHORT).show();
            }

            availableAmount = totalBudget - totalSpends;
            SpendsSummary spendsSummary = new SpendsSummary(availableAmount.toString(), totalBudget.toString(), totalSpends.toString());
            return spendsSummary;
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return new SpendsSummary("0", "0", "0");
        }
    }

    public Cursor getTransactions() {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        DateRange dateRange = userLocalStore.getDateRange();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();

        String email = userLocalStore.getLoggedInUser().email;
        String startDateString = dateTimeHelper.getInsertString(dateRange.startDateObject);
        String endDateString = dateTimeHelper.getInsertString(dateRange.endDateObject);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String query;

        try {
            query = "SELECT * FROM Transactions WHERE Email = ? AND TransactionDate BETWEEN ? AND ? ORDER BY TransactionDate DESC";
            cursor = db.rawQuery(query, new String[] {email, startDateString, endDateString});
            return cursor;
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public HashMap<String, String> getTransactionSummaryHashMap() {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        DateRange dateRange = userLocalStore.getDateRange();
        DateTimeHelper dateTimeHelper = new DateTimeHelper();

        String email = userLocalStore.getLoggedInUser().email;
        String startDateString = dateTimeHelper.getInsertString(dateRange.startDateObject);
        String endDateString = dateTimeHelper.getInsertString(dateRange.endDateObject);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        String query;

        HashMap<String, String> transactionSummary = new HashMap<>();

        try {
            query = "SELECT TransactionCategory, SUM(TransactionAmount) AS TotalAmount FROM Transactions WHERE TransactionType = 'Spends' AND Email = ? AND TransactionDate BETWEEN ? AND ? GROUP BY TransactionCategory ORDER BY SUM(TransactionAmount) DESC";
            cursor = db.rawQuery(query, new String[] {email, startDateString, endDateString});

            if(cursor == null || cursor.getCount() == 0) {
                return null;
            }
            else {
                while(cursor.moveToNext()) {
                    String categoryName = cursor.getString(0);
                    Integer totalAmount = cursor.getInt(1);

                    transactionSummary.put(categoryName, totalAmount.toString());
                }
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }

        return transactionSummary;
    }

    /* -------------------------------------------------------------------------------Device to Server-------------------------------------------------------------------------- */
    public Cursor getDirtyUser() {
        UserLocalStore userLocalStore = new UserLocalStore(context);

        String email = userLocalStore.getLoggedInUser().email;

        Cursor res = null;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM Users WHERE Email = '" + email + "' AND IsDirty = 1";
            res = db.rawQuery(query, null);
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        finally {
            return res;
        }
    }

    public void makeUserNotDirty() {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        String email = userLocalStore.getLoggedInUser().email;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IsDirty", 0);
        db.update(USERS_TABLE_NAME, contentValues, "Email = '" + email + "'", null);
    }

    public Cursor getDirtyCategories() {
        UserLocalStore userLocalStore = new UserLocalStore(context);

        String email = userLocalStore.getLoggedInUser().email;

        Cursor res = null;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM Categories WHERE Email = '" + email + "' AND IsDirty = 1";
            res = db.rawQuery(query, null);
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        finally {
            return res;
        }
    }

    public void makeCategoriesNotDirty() {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        String email = userLocalStore.getLoggedInUser().email;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IsDirty", 0);
        db.update(CATEGORIES_TABLE_NAME, contentValues, "Email = '" + email + "'", null);
    }

    public Cursor getDirtyTransactions() {
        UserLocalStore userLocalStore = new UserLocalStore(context);

        String email = userLocalStore.getLoggedInUser().email;

        Cursor res = null;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM Transactions WHERE Email = '" + email + "' AND IsDirty = 1";
            res = db.rawQuery(query, null);
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        finally {
            return res;
        }
    }

    public void makeTransactionsNotDirty() {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        String email = userLocalStore.getLoggedInUser().email;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IsDirty", 0);
        db.update(TRANSACTIONS_TABLE_NAME, contentValues, "Email = '" + email + "'", null);
    }

    /* -------------------------------------------------------------------------------Server to Device-------------------------------------------------------------------------- */

    public void getUserFromServer(User user)
    {
        SyncServerToDeviceServerRequests syncServerToDeviceServerRequests = new SyncServerToDeviceServerRequests(context);
        syncServerToDeviceServerRequests.fetchUserInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    Toast.makeText(context, "Invalid Email / Password", Toast.LENGTH_LONG).show();
                } else {
                    insertUserFromServerOnLocalDbAndLogin(returnedUser);
                }
            }
        });
    }

    void insertUserFromServerOnLocalDbAndLogin(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email", user.email);
        contentValues.put("Mobile", user.mobile);
        contentValues.put("Name", user.name);
        contentValues.put("Password", user.password);
        contentValues.put("IsDirty", 0);
        long res = db.insert(USERS_TABLE_NAME, null, contentValues);
        if(res == -1) {
            Toast.makeText(context, "Unable to insert the User retrieved from server into local db", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "User successfully Synced from Server", Toast.LENGTH_SHORT).show();
            logUserIn(user);
        }
    }

    void insertCategoriesFromServer(String json_string) {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        String email = userLocalStore.getLoggedInUser().email;

        SQLiteDatabase db = this.getWritableDatabase();
        org.json.JSONObject jsonObject;
        org.json.JSONArray jsonArray;

        int noOfCategoriesInsertedFromServer = 0;
        if(json_string != null) { // to avoid null pointer exception
            try {
                jsonObject = new org.json.JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("server_response");

                int count = 0;
                String categoryName, categoryType;

                while(count < jsonArray.length())
                {
                    org.json.JSONObject jo = jsonArray.getJSONObject(count);

                    categoryName = jo.getString("CategoryName");
                    categoryType = jo.getString("CategoryType");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Email", email);
                    contentValues.put("CategoryName", categoryName);
                    contentValues.put("CategoryType", categoryType);
                    contentValues.put("IsDirty", 0);

                    long res = db.insert(CATEGORIES_TABLE_NAME, null, contentValues);
                    if(res == -1) {
                        //do nothing
                    }
                    else {
                        noOfCategoriesInsertedFromServer++;
                    }
                    count++;
                }
            }catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        if(noOfCategoriesInsertedFromServer == 1) {
            Toast.makeText(context, noOfCategoriesInsertedFromServer + " Category Synced from server", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, noOfCategoriesInsertedFromServer + " Categories Synced from server", Toast.LENGTH_SHORT).show();
        }
    }

    void insertTransactionsFromServer(String json_string) {
        UserLocalStore userLocalStore = new UserLocalStore(context);
        String email = userLocalStore.getLoggedInUser().email;

        SQLiteDatabase db = this.getWritableDatabase();
        org.json.JSONObject jsonObject;
        org.json.JSONArray jsonArray;

        int noOfTransactionsInsertedFromServer = 0;
        if(json_string != null) { // to avoid null pointer exception
            try {
                jsonObject = new org.json.JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("server_response");

                int count = 0;
                String transactionType, transactionDate, transactionCategory, transactionAmount, transactionDescription;

                while(count < jsonArray.length())
                {
                    org.json.JSONObject jo = jsonArray.getJSONObject(count);

                    transactionType = jo.getString("TransactionType");
                    transactionDate = jo.getString("TransactionDate");
                    transactionCategory = jo.getString("TransactionCategory");
                    transactionAmount = jo.getString("TransactionAmount");
                    transactionDescription = jo.getString("TransactionDescription");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Email", email);
                    contentValues.put("TransactionType", transactionType);
                    contentValues.put("TransactionDate", transactionDate);
                    contentValues.put("TransactionCategory", transactionCategory);
                    contentValues.put("TransactionAmount", transactionAmount);
                    contentValues.put("TransactionDescription", transactionDescription);
                    contentValues.put("IsDirty", 0);

                    long res = db.insert(TRANSACTIONS_TABLE_NAME, null, contentValues);
                    if(res == -1) {
                        //do nothing
                    }
                    else {
                        noOfTransactionsInsertedFromServer++;
                    }
                    count++;
                }
            }catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        if(noOfTransactionsInsertedFromServer == 1) {
            Toast.makeText(context, noOfTransactionsInsertedFromServer + " Transaction Synced from server", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, noOfTransactionsInsertedFromServer + " Transactions Synced from server", Toast.LENGTH_SHORT).show();
        }
    }

    /*------------------------------------------------------------------------------Transaction from SMS-----------------------------------------------------------*/
    void insertPurchaseFromSMS(HashMap<String, String> purchaseDetails) {
        HelperClass helperClass = new HelperClass(context);
        DateTimeHelper dateTimeHelper = new DateTimeHelper();

        Cursor res = null;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String loginQuery = "SELECT * FROM Users";
            res = db.rawQuery(loginQuery, null);
            if(res == null || res.getCount() == 0) {
                Toast.makeText(context, "No User Registered on this Device", Toast.LENGTH_LONG).show();
            }
            else {
                while(res.moveToNext()) {
                    String email = res.getString(0);
                    String transactionDate = dateTimeHelper.getInsertString(new Date());

                    Transaction transaction = new Transaction(email, purchaseDetails.get("Amount"), helperClass.getCategory(purchaseDetails.get("MerchantName")), transactionDate, "at " + purchaseDetails.get("MerchantName"), "Spends");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Email", transaction.email);
                    contentValues.put("TransactionType", transaction.transactionType);
                    contentValues.put("TransactionDate", transaction.transactionDate);
                    contentValues.put("TransactionDate", transaction.transactionDate);
                    contentValues.put("TransactionCategory", transaction.transactionCategory);
                    contentValues.put("TransactionAmount", transaction.transactionAmount);
                    contentValues.put("TransactionDescription", transaction.transactionDescription);
                    contentValues.put("IsDirty", 1);

                    long status = db.insert(TRANSACTIONS_TABLE_NAME, null, contentValues);
                    if(status == -1) {
                        Toast.makeText(context, "This transaction is already added", Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(context, "Transaction successfully added from SMS", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
