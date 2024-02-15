package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findViewById(R.id.password_icon_registration).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TextView password = findViewById(R.id.password_registration);
                LoginActivity.onTouch(view, motionEvent, password);
                return true;
            }
        });

        findViewById(R.id.confirm_password_icon_registration).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TextView password = findViewById(R.id.password_confirm_registration);
                LoginActivity.onTouch(view, motionEvent, password);
                return true;
            }
        });
    }

    public void signUp(View v) {
        Intent signUp = new Intent(this, LoginActivity.class);
        startActivity(signUp);
    }

    public void registration(View v) {
        TextView passwordView = findViewById(R.id.password_registration);
        TextView confirmPasswordView = findViewById(R.id.password_confirm_registration);
        TextView nameView = findViewById(R.id.name_registration);
        TextView emailView = findViewById(R.id.email_registration);

        if (passwordView.getText().toString().equals(confirmPasswordView.getText().toString())) {
            if (emailView.getText().toString().endsWith("@mail.ru") || emailView.getText().toString().endsWith("@gmail.com") ||
                    emailView.getText().toString().endsWith("@tut.by")) {
                LoginActivity.login = nameView.getText().toString();
                String password = passwordView.getText().toString();
                String email = emailView.getText().toString();

                SQLiteDatabase database = DataBase.getDatabase(this);

                String[] projection = {DBHelper.KEY_ID};
                String selection = DBHelper.KEY_NAME + " = ?";
                String[] selectionArgs = {LoginActivity.login};
                Cursor cursor = database.query(DBHelper.TABLE_NAME, projection, selection,selectionArgs, null, null, null);

                if (cursor.moveToFirst() || LoginActivity.login.equals("admin")) {
                    TextView error = findViewById(R.id.error_registration);
                    error.setText("Такой пользователь уже существует");
                } else {
                    DataBase.insertData(password, email);
                    DataBase.LoadData();

                    if (AchievementActivity.achievementReg()) AchievementActivity.showMessage(this);
                    cursor.close();

                    Intent intent = new Intent(this, HomePage.class);
                    startActivity(intent);
                }
            } else {
                TextView error = findViewById(R.id.error_registration);
                error.setText("Неверный формат электронной почты");
            }
        } else {
            TextView error = findViewById(R.id.error_registration);
            error.setText("Пароли не совпадают");
        }
    }
}