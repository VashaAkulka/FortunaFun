package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AchievementActivity extends AppCompatActivity {

    final int COUNT_ACHIEVEMENT = 12;
    LinearLayout[] linearLayouts;
    TextView[] textViews;
    List<Integer> lisOfAchievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        Menu.MenuInit(findViewById(R.id.navigation_achievement), this);
        lisOfAchievement = DataBase.getAchievement();

        linearLayouts = new LinearLayout[]{findViewById(R.id.achievement_1), findViewById(R.id.achievement_2), findViewById(R.id.achievement_3),
                findViewById(R.id.achievement_4), findViewById(R.id.achievement_5), findViewById(R.id.achievement_6), findViewById(R.id.achievement_7),
                findViewById(R.id.achievement_8), findViewById(R.id.achievement_9), findViewById(R.id.achievement_10), findViewById(R.id.achievement_11),
                findViewById(R.id.achievement_12), findViewById(R.id.achievement_13), findViewById(R.id.achievement_14), findViewById(R.id.achievement_15),
                findViewById(R.id.achievement_16)};

        textViews = new TextView[]{findViewById(R.id.achievement_percent_1), findViewById(R.id.achievement_percent_2), findViewById(R.id.achievement_percent_3),
                findViewById(R.id.achievement_percent_4), findViewById(R.id.achievement_percent_5), findViewById(R.id.achievement_percent_6), findViewById(R.id.achievement_percent_7),
                findViewById(R.id.achievement_percent_8),findViewById(R.id.achievement_percent_9),findViewById(R.id.achievement_percent_10),findViewById(R.id.achievement_percent_11),
                findViewById(R.id.achievement_percent_12),findViewById(R.id.achievement_percent_13),findViewById(R.id.achievement_percent_14),findViewById(R.id.achievement_percent_15),
                findViewById(R.id.achievement_percent_16)};

        LightAchievement();
        calculatePercent();
    }

    public void navigationAchievement(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_achievement);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void LightAchievement() {
        for (int i = 0; i < COUNT_ACHIEVEMENT; i++) {
            if (lisOfAchievement.contains(i)) {
                linearLayouts[i].setAlpha(1);
            }
        }
    }

    public void calculatePercent() {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setText("" + DataBase.getPercent(i) + "%");
        }
    }

    public static boolean achievementReg() {
        return DataBase.putAchievement(0);
    }
    public static boolean achievementBonus() {
        return DataBase.putAchievement(1);
    }
    public static boolean achievementFirstBet() {
        return DataBase.putAchievement(2);
    }
    public static boolean achievementMoney() {
        if (Fantiki.currentFantiki >= 1000000) {
            return DataBase.putAchievement(6);
        } else if (Fantiki.currentFantiki >= 100000) {
            return DataBase.putAchievement(5);
        } else if (Fantiki.currentFantiki >= 10000) {
            return DataBase.putAchievement(4);
        } else if (Fantiki.currentFantiki >= 1000) {
            return DataBase.putAchievement(3);
        }
        return false;
    }

    public static boolean achievementCount(int count) {
        if (count == 12) {
            return DataBase.putAchievement(8);
        } else if (count == 4) {
            return DataBase.putAchievement(7);
        }
        return false;
    }

    public static boolean achievementAllIn() {
        if (Fantiki.currentFantiki <= 1) {
            DataBase.putAchievement(9);
            return true;
        }
        return false;
    }

    public static boolean achievementUnluck(double bet1, double bet2, double bet3, double win) {
        if (bet1 > 0 && bet2 > 0 && bet3 == 0 && win == 0) {
            return DataBase.putAchievement(10);
        } else if (bet1 > 0 && bet2 == 0 && bet3 > 0 && win == 0) {
            return DataBase.putAchievement(10);
        } else if (bet1 == 0 && bet2 > 0 && bet3 > 0 && win == 0) {
            return DataBase.putAchievement(10);
        }
        return false;
    }

    public static boolean achievementMommy() {
        return DataBase.putAchievement(11);
    }

    public static void showMessage(Context context) {
        Toast.makeText(context, "Вы получили ачивку!", Toast.LENGTH_LONG).show();
    }


    public static boolean allinnaBeloe(double navsebabki,double currency){
        if(navsebabki==currency){
            return DataBase.putAchievement(14);
        }
        return false;
    }
    public static boolean achivmentThreevriad(int count){
        if(count ==1){
            return  DataBase.putAchievement(12);
        }
        return false;
    }
    public static boolean achivmentcoins(int count){
        if (count == 10){
            return DataBase.putAchievement(13);

        }        return false;
    }
    public static boolean achievementSeven(int count) {
        if(count == 4){
            return DataBase.putAchievement(15);
        }
        return false;
    }
}