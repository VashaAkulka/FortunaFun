package com.example.roulet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.security.Key;

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
        contentValues.put(DBHelper.KEY_FANTIKI, 100.00);

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



    private static <T> T[] getColumnData(String columnName, Class<T> clazz) {
        String[] projection = {columnName};
        String sortOrder = columnName + " DESC";
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, null, null, null, null, sortOrder, "10");

        T[] data = (T[]) Array.newInstance(clazz, cursor.getCount());
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(columnName);
            int i = 0;
            do {
                if (clazz.equals(String.class)) data[i++] = (T) cursor.getString(index);
                else data[i++] = (T) Double.valueOf(cursor.getDouble(index));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return data;
    }

    public static String[] GetArrayUserName() {
        return getColumnData(DBHelper.KEY_NAME, String.class);
    }

    public static double[] GetArrayUserFantik() {
        Double[] doubleData = getColumnData(DBHelper.KEY_FANTIKI, Double.class);

        double[] data = new double[doubleData.length];
        for (int i = 0; i < doubleData.length; i++) {
            data[i] = doubleData[i].doubleValue();
        }

        return data;
    }

}
