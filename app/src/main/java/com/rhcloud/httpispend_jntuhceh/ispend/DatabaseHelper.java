package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

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
    static final String CREATE_CATEGORIES_TABLE_QUERY = "CREATE TABLE Categories (Email varchar(100) NOT NULL, CategoryName varchar(50) NOT NULL, CategoryType varchar(20) NOT NULL DEFAULT 'Spends', CONSTRAINT CategoriesConstraint UNIQUE(Email, CategoryName, CategoryType))";

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
            String[] categoriesArray = {"Food", "Entertainment", "Electronics", "Clothing", "Footwear" , "Miscellaneous"};
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

    public User validateLogin(User user) {
        Cursor res = null;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String loginQuery = "SELECT * FROM Users WHERE Email = '" + user.email + "' AND Password = '" + user.password + "'";
            res = db.rawQuery(loginQuery, null);
            if(res == null || res.getCount() == 0) {
                return null;
            }
            else {
                if(res.moveToNext()) {
                    user.name = res.getString(2);
                    user.mobile = res.getString(1);
                }
                return user;
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return null;
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

    public TransactionSummary getTransactionSummary() {
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
            TransactionSummary transactionSummary = new TransactionSummary(availableAmount.toString(), totalBudget.toString(), totalSpends.toString());
            return transactionSummary;
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return new TransactionSummary("0", "0", "0");
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
            query = "SELECT * FROM Transactions WHERE Email = ? AND TransactionDate BETWEEN ? AND ?";
            cursor = db.rawQuery(query, new String[] {email, startDateString, endDateString});
            return cursor;
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
