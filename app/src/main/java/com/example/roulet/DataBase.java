package com.example.roulet;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public static void updateData(double currentValue, Context context, double win, double bet) {
        if (AchievementActivity.achievementMoney()) AchievementActivity.showMessage(context);

        String[] projection = {DBHelper.KEY_WIN, DBHelper.KEY_PLAY};
        String selection = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgs = {LoginActivity.login};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        int indexWin = cursor.getColumnIndex(DBHelper.KEY_WIN);
        int indexBet = cursor.getColumnIndex(DBHelper.KEY_PLAY);

        cursor.moveToFirst();

        Double newWin = new BigDecimal(win).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Double newBet = new BigDecimal(bet).setScale(2, RoundingMode.HALF_UP).doubleValue();

        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_FANTIKI, currentValue);
        values.put(DBHelper.KEY_WIN, cursor.getDouble(indexWin) + newWin);
        values.put(DBHelper.KEY_PLAY, cursor.getDouble(indexBet) + newBet);

        String selectionUpdate = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgsUpdate = {LoginActivity.login};

        database.update(DBHelper.TABLE_NAME, values, selectionUpdate, selectionArgsUpdate);
        cursor.close();
    }

    public static void insertData(String password, String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, LoginActivity.login);
        contentValues.put(DBHelper.KEY_PASSWORD, password);
        contentValues.put(DBHelper.KEY_MAIL, email);
        contentValues.put(DBHelper.KEY_FANTIKI, 100.00);
        contentValues.put(DBHelper.KEY_WIN, 0);
        contentValues.put(DBHelper.KEY_PLAY, 0);

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
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(10)
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
         String selection = DBHelper.KEY_ID_ACHIEVEMENT + " = ?";
         String[] selectionArgs = {String.valueOf(idAchievement)};
         String Query = "SELECT COUNT(*) FROM " + DBHelper.TABLE_NAME_ACHIEVEMENT + " WHERE " + selection;
         Cursor cursorAchievement = database.rawQuery(Query, selectionArgs);
         cursorAchievement.moveToFirst();
         int countAchievement = cursorAchievement.getInt(0);
         cursorAchievement.close();

         return (int) ((double) countAchievement / countAllUser() * 100);
    }


    public static View setCustomAdapter(int i, View view, CustomAdminAdapter adapter) {
        TextView textId = view.findViewById(R.id.admin_id);
        TextView textName = view.findViewById(R.id.admin_name);
        TextView textPassword = view.findViewById(R.id.admin_password);
        TextView textMail = view.findViewById(R.id.admin_mail);
        TextView textBalance = view.findViewById(R.id.admin_balance);

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToPosition(i);

        int id = cursor.getColumnIndex(DBHelper.KEY_ID);
        int name = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int password = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
        int mail = cursor.getColumnIndex(DBHelper.KEY_MAIL);
        int balance = cursor.getColumnIndex(DBHelper.KEY_FANTIKI);

        textId.setText(String.valueOf(cursor.getInt(id)));
        textName.setText(cursor.getString(name));
        textPassword.setText(cursor.getString(password));
        textMail.setText(cursor.getString(mail));
        textBalance.setText(String.valueOf(cursor.getDouble(balance)));

        ImageView imageTrash = view.findViewById(R.id.admin_trash);
        imageTrash.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.trash_dialog);
            Button yes = dialog.findViewById(R.id.dialog_trash_yes);
            Button no = dialog.findViewById(R.id.dialog_trash_no);

            yes.setOnClickListener(viewClick -> {
                DataBase.deleteUser(cursor.getInt(id));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            });

            no.setOnClickListener(viewClick -> {
                dialog.dismiss();
            });

            dialog.show();
        });

        ImageView imageEdit = view.findViewById(R.id.admin_edit);
        imageEdit.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.edit_dialog);

            Button yes = dialog.findViewById(R.id.dialog_edit);
            Button no = dialog.findViewById(R.id.dialog_edit_no);
            EditText editText = dialog.findViewById(R.id.edit_value);

            yes.setOnClickListener(viewClick -> {
                try {
                    double currentValue = Double.parseDouble(editText.getText().toString());

                    DataBase.editFantiki(cursor.getInt(id), currentValue);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                } catch (NumberFormatException err) {

                }
            });

            no.setOnClickListener(viewClick -> {
                dialog.dismiss();
            });

            dialog.show();
        });

        return view;
    }

    public static int countAllUser() {
        String countQuery = "SELECT COUNT(*) FROM " + DBHelper.TABLE_NAME;
        Cursor cursorAll = database.rawQuery(countQuery, null);
        if (cursorAll.moveToFirst()) {
            return cursorAll.getInt(0);
        }
        return 0;
    }

    public static void deleteUser(int i) {
        String selection = DBHelper.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(i)};

        String deleteQuery = "DELETE FROM " + DBHelper.TABLE_NAME_ACHIEVEMENT + " WHERE " + DBHelper.KEY_NAME_USER
                + " IN (SELECT " + DBHelper.KEY_NAME + " FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.KEY_ID + " = ?)";
        database.execSQL(deleteQuery, selectionArgs);

        deleteQuery = "DELETE FROM " + DBHelper.TABLE_NAME_PLAY + " WHERE " + DBHelper.KEY_NAME_USER
                + " IN (SELECT " + DBHelper.KEY_NAME + " FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.KEY_ID + " = ?)";
        database.execSQL(deleteQuery, selectionArgs);

        database.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
    }

    public static void editFantiki(int i, double currentValue) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_FANTIKI, currentValue);

        String selection = DBHelper.KEY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(i)};

        database.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
    }

    public static void editAvatar(String uri) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_AVATAR, uri);

        String selection = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgs = {LoginActivity.login};

        database.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
    }

    public static String takeAvatar() {
        String[] projection = {DBHelper.KEY_AVATAR};
        String selection = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgs = {LoginActivity.login};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        } else return null;
    }

    public static void setText(View view) {
        String[] projection = {DBHelper.KEY_WIN, DBHelper.KEY_PLAY};
        String selection = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgs = {LoginActivity.login};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        int indexWin = cursor.getColumnIndex(DBHelper.KEY_WIN);
        int indexBet = cursor.getColumnIndex(DBHelper.KEY_PLAY);

        cursor.moveToFirst();
        TextView name = view.findViewById(R.id.profile_name);
        TextView balance = view.findViewById(R.id.profile_balance);
        TextView win = view.findViewById(R.id.profile_win);
        TextView bet = view.findViewById(R.id.profile_bet);

        name.setText(LoginActivity.login);
        balance.setText("Баланс: " + Fantiki.currentFantiki);
        win.setText("Выйграно: " + cursor.getDouble(indexWin));
        bet.setText("Поставлено: " + cursor.getDouble(indexBet));
    }

    public static void addToHistory(double bet, double win, String slot) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_NAME_USER, LoginActivity.login);
        contentValues.put(DBHelper.KEY_BET_PLAY, bet);
        contentValues.put(DBHelper.KEY_WIN_PLAY, win);
        contentValues.put(DBHelper.KEY_SLOT_PLAY, slot);

        database.insert(DBHelper.TABLE_NAME_PLAY, null, contentValues);
    }

    public static int countHistory() {
        String countQuery = "SELECT COUNT(*) FROM " + DBHelper.TABLE_NAME_PLAY + " WHERE " + DBHelper.KEY_NAME_USER + " = ?";
        Cursor cursorAll = database.rawQuery(countQuery, new String[]{LoginActivity.login});
        if (cursorAll.moveToFirst()) {
            return cursorAll.getInt(0);
        }
        return 0;
    }

    public static View setCustomAdapterHistory(int i, View view) {
        TextView textBet = view.findViewById(R.id.profile_bet);
        TextView textWin = view.findViewById(R.id.profile_win);
        TextView textSlot = view.findViewById(R.id.profile_slot);
        TextView textPercent = view.findViewById(R.id.profile_percent);

        String[] projection = {DBHelper.KEY_WIN_PLAY, DBHelper.KEY_BET_PLAY, DBHelper.KEY_SLOT_PLAY};
        String selection = DBHelper.KEY_NAME_USER + " = ?";
        String[] selectionArgs = {LoginActivity.login};
        Cursor cursor = database.query(DBHelper.TABLE_NAME_PLAY, projection, selection, selectionArgs, null, null, null);

        cursor.moveToPosition(countHistory() - i - 1);
        int win = cursor.getColumnIndex(DBHelper.KEY_WIN_PLAY);
        int bet = cursor.getColumnIndex(DBHelper.KEY_BET_PLAY);
        int slot = cursor.getColumnIndex(DBHelper.KEY_SLOT_PLAY);

        textBet.setText(String.valueOf(cursor.getDouble(bet)));
        textWin.setText(String.valueOf(cursor.getDouble(win)));
        textSlot.setText(cursor.getString(slot));

        Double badPercent = cursor.getDouble(win) / cursor.getDouble(bet);
        Double percent = new BigDecimal(badPercent).setScale(2, RoundingMode.HALF_UP).doubleValue();
        textPercent.setText(percent + "x");

        return view;
    }

    public static void deleteUserHistory() {
        String deleteQuery = "DELETE FROM " + DBHelper.TABLE_NAME_PLAY + " WHERE " + DBHelper.KEY_NAME_USER + " = ?";
        database.execSQL(deleteQuery, new String[]{LoginActivity.login});
    }

    public static void setProfileLeaderBord(Dialog dialog, String name) {
        TextView nameView = dialog.findViewById(R.id.profile_name_dialog);
        TextView balanceView = dialog.findViewById(R.id.profile_balance_dialog);
        TextView betView = dialog.findViewById(R.id.profile_bet_dialog);
        TextView winView = dialog.findViewById(R.id.profile_win_dialog);
        ImageView avatar = dialog.findViewById(R.id.profile_avatar_dialog);

        String[] projection = {DBHelper.KEY_AVATAR, DBHelper.KEY_FANTIKI, DBHelper.KEY_WIN, DBHelper.KEY_PLAY};
        String selection = DBHelper.KEY_NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        int indexWin = cursor.getColumnIndex(DBHelper.KEY_WIN);
        int indexBet = cursor.getColumnIndex(DBHelper.KEY_PLAY);
        int indexAvatar = cursor.getColumnIndex(DBHelper.KEY_AVATAR);
        int indexBalance = cursor.getColumnIndex(DBHelper.KEY_FANTIKI);

        cursor.moveToFirst();
        nameView.setText(name);
        balanceView.setText("Баланс: " + cursor.getDouble(indexBalance));
        winView.setText("Выйграно: " + cursor.getDouble(indexWin));
        betView.setText("Поставлено: " + cursor.getDouble(indexBet));

        if (cursor.getString(indexAvatar) != null) {
            avatar.setImageURI(Uri.parse(cursor.getString(indexAvatar)));
        }
    }
}
