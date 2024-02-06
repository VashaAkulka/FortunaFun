package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                findViewById(R.id.achievement_12)};

        textViews = new TextView[]{findViewById(R.id.achievement_percent_1), findViewById(R.id.achievement_percent_2), findViewById(R.id.achievement_percent_3),
                findViewById(R.id.achievement_percent_4), findViewById(R.id.achievement_percent_5), findViewById(R.id.achievement_percent_6), findViewById(R.id.achievement_percent_7),
                findViewById(R.id.achievement_percent_8),findViewById(R.id.achievement_percent_9),findViewById(R.id.achievement_percent_10),findViewById(R.id.achievement_percent_11),
                findViewById(R.id.achievement_percent_12),};

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

    public static void achievementReg() {
        DataBase.putAchievement(0);
    }
}