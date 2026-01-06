package com.example.openendedlab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final int DATABASE_VERSION = 2; // Updated version for schema change
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_USER_EMAIL = "user_email"; // New column

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_EXPENSES + " ADD COLUMN " + COLUMN_USER_EMAIL + " TEXT");
        }
    }

    public void addExpense(Expense expense, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, expense.getTitle());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_DATE, expense.getDate());
        values.put(COLUMN_USER_EMAIL, userEmail);
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public List<Expense> getAllExpenses(String userEmail) {
        List<Expense> expenseList = new ArrayList<>();
        // Filter by user email
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_USER_EMAIL + " = ? ORDER BY " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                expenseList.add(new Expense(id, title, amount, date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseList;
    }
}
