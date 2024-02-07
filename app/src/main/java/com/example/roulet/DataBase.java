package com.example.roulet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private static SQLiteDatabase database;

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            DBHelper dbHelper = new DBHelper(context);
            database = dbHelper.getWritableDatabase();
        }
        return database;
    }

    public static void updateData(double currentValue, Context context) {
        if (AchievementActivity.achievementMoney()) AchievementActivity.showMessage(context);

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

    public static Map<String, Double> GetUserData() {
        String[] projection = {DBHelper.KEY_NAME, DBHelper.KEY_FANTIKI};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, null, null, null, null, null);

        int indexName = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int indexFantiki = cursor.getColumnIndex(DBHelper.KEY_FANTIKI);
        Map<String, Double> map = new HashMap<>();

        cursor.moveToFirst();
        do {
            map.put(cursor.getString(indexName), cursor.getDouble(indexFantiki));
        } while (cursor.moveToNext());

        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }

    public static boolean putAchievement(int id) {
        String[] projection = {DBHelper.KEY_ID_ACHIEVEMENT};
        String selection = DBHelper.KEY_ID_ACHIEVEMENT + " = ? AND " + DBHelper.KEY_NAME_USER + " = ?";
        String[] selectionArgs = {String.valueOf(id), LoginActivity.login};
        Cursor cursor = database.query(DBHelper.TABLE_NAME_ACHIEVEMENT, projection, selection, selectionArgs, null, null, null);

        if (!cursor.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_NAME_USER, LoginActivity.login);
            contentValues.put(DBHelper.KEY_ID_ACHIEVEMENT, id);

            database.insert(DBHelper.TABLE_NAME_ACHIEVEMENT, null, contentValues);
            cursor.close();
            return true;
        }
        return false;
    }

    public static List<Integer> getAchievement() {
        String[] projection = {DBHelper.KEY_ID_ACHIEVEMENT};
        String selection = DBHelper.KEY_NAME_USER + " = ?";
        String[] selectionArgs = {LoginActivity.login};
        Cursor cursor = database.query(DBHelper.TABLE_NAME_ACHIEVEMENT, projection, selection, selectionArgs, null, null, null);

        List<Integer> idAchievement = new ArrayList<>();
        if(cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(DBHelper.KEY_ID_ACHIEVEMENT);
            do {
                idAchievement.add(cursor.getInt(index));
            } while(cursor.moveToNext());
        }
        cursor.close();

        return idAchievement;
     }

     public static int getPercent(int idAchievement) {
         String countQuery = "SELECT COUNT(*) FROM " + DBHelper.TABLE_NAME;
         Cursor cursorAll = database.rawQuery(countQuery, null);
         cursorAll.moveToFirst();
         int countAll = cursorAll.getInt(0);
         cursorAll.close();

         String selection = DBHelper.KEY_ID_ACHIEVEMENT + " = ?";
         String[] selectionArgs = {String.valueOf(idAchievement)};
         String Query = "SELECT COUNT(*) FROM " + DBHelper.TABLE_NAME_ACHIEVEMENT + " WHERE " + selection;
         Cursor cursorAchievement = database.rawQuery(Query, selectionArgs);
         cursorAchievement.moveToFirst();
         int countAchievement = cursorAchievement.getInt(0);
         cursorAchievement.close();

         return (int) ((double) countAchievement / countAll * 100);
     }
}
