package com.example.roulet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "NoKazikDB";


    public static final String TABLE_NAME = "UsersData";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MAIL = "mail";
    public static final String KEY_FANTIKI = "fantiki";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_WIN = "win";
    public static final String KEY_PLAY = "play";


    public static final String TABLE_NAME_ACHIEVEMENT = "Achievement";

    public static final String KEY_ID_ACHIEVEMENT = "id_achievement";

    public static final String KEY_NAME_USER = "name_user";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + KEY_ID + " integer primary key, " + KEY_NAME + " text, " + KEY_PASSWORD + " text, " +
                KEY_MAIL + " text, " + KEY_FANTIKI + " real, " + KEY_AVATAR + " text, " + KEY_PLAY + " real, " + KEY_WIN + " real)");

        db.execSQL("create table " + TABLE_NAME_ACHIEVEMENT + " (" + KEY_NAME_USER + " text, " + KEY_ID_ACHIEVEMENT + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        db.execSQL("drop table if exists " + TABLE_NAME_ACHIEVEMENT);
        onCreate(db);
    }
}
