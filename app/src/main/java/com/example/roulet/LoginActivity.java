package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    static String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.password_icon_login).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TextView password = findViewById(R.id.password_login);
                LoginActivity.onTouch(view, motionEvent, password);
                return true;
            }
        });
    }

    public void signIn(View v) {
        Intent signIn = new Intent(this, RegistrationActivity.class);
        startActivity(signIn);
    }

    public static void onTouch(View view, MotionEvent motionEvent, TextView password) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case MotionEvent.ACTION_UP:
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        }
    }

    public void login(View v) {
        TextView loginView = findViewById(R.id.name_login);
        login = loginView.getText().toString();

        TextView passwordView = findViewById(R.id.password_login);
        String password = passwordView.getText().toString();

        SQLiteDatabase database = DataBase.getDatabase(this);


        String[] projection = {DBHelper.KEY_FANTIKI};
        String selection = DBHelper.KEY_NAME + " = ? AND " + DBHelper.KEY_PASSWORD + " = ?";
        String[] selectionArgs = {login, password};
        Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            DataBase.LoadData();

            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        } else {
            TextView error = findViewById(R.id.error_login);
            error.setText("Проверьте введенный данные");
        }

        cursor.close();
    }
}