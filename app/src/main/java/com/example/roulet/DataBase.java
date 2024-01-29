package com.example.roulet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBase {
    private static SQLiteDatabase database;

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            DBHelper dbHelper = new DBHelper(context);
            database = dbHelper.getWritableDatabase();
        }
        return database;
    }

    public static void updateData(double currentValue) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_FANTIKI, currentValue);

        String selection = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgs = {LoginActivity.login};

        database.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
    }

    public static void insertData(String password, String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, LoginActivity.login);
        contentValues.put(DBHelper.KEY_PASSWORD, password);
        contentValues.put(DBHelper.KEY_MAIL, email);
        contentValues.put(DBHelper.KEY_FANTIKI, 1000.00);

        database.insert(DBHelper.TABLE_NAME, null, contentValues);
    }

    public static void LoadData() {
        String[] projection = {DBHelper.KEY_FANTIKI};
        String selection = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgs = {LoginActivity.login};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        int index = cursor.getColumnIndex(DBHelper.KEY_FANTIKI);
        Fantiki.currentFantiki = cursor.getDouble(index);

        cursor.close();
    }
}
