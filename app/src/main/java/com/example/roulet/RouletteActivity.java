package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class RouletteActivity extends AppCompatActivity {

    private int degree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        Menu.MenuInit(findViewById(R.id.navigation_roulette), this);
        Music.musicInit(this, R.raw.music);

        Fantiki.ViewFantiki(findViewById(R.id.balanceView));
        Fantiki.ViewBet(findViewById(R.id.betView));

        findViewById(R.id.buttonMinus).setOnLongClickListener(view -> {
            Fantiki.bet = 1;
            Fantiki.ViewBet(findViewById(R.id.betView));
            return true;
        });

        findViewById(R.id.buttonPlus).setOnLongClickListener(view -> {
            Fantiki.bet = Fantiki.currentFantiki;
            Fantiki.ViewBet(findViewById(R.id.betView));
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Music.musicOFF();
    }

    public void changeBet(View v) {
        if (v.getId() == R.id.buttonMinus) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
        }
        else {
            double buff = Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
            if (buff + Fantiki.bet <= Fantiki.currentFantiki) Fantiki.bet += buff;
        }

        Fantiki.ViewBet(findViewById(R.id.betView));
    }

    public void rotateRoulette(View v) {
        degree += (Math.random() * 360) + 1440;
        findViewById(R.id.roulette).animate().rotation(degree).setDuration(4000).withEndAction(() -> {
            int number = (degree % 360) / 40;
            String color = null;

            switch (number) {
                case 0:
                case 2:
                case 5:
                case 7:
                    color = "#e8696b";
                    break;
                case 1:
                case 3:
                case 6:
                case 8:
                    color ="#5d6766";
                    break;
                case 4:
                    color = "#62ae34";
            }

            Fantiki.loseOrWin_Roulette(color, findViewById(R.id.winningView), this);
            Fantiki.ViewFantiki(findViewById(R.id.balanceView));

            v.setBackgroundColor(Color.parseColor(color));
            ((Button) v).setText("Сделайте ставку");

            clearBetView();
            Fantiki.ZeroBetOnColor();
            switchEnableButton(true);
            findViewById(R.id.restartBet).setEnabled(true);
            if (AchievementActivity.achievementFirstBet()) AchievementActivity.showMessage(this);
        }).withStartAction(() -> {
            switchEnableButton(false);
            v.setEnabled(false);
            findViewById(R.id.restartBet).setEnabled(false);
        });

    }

    public void betOnColor(View v) {
        TextView betOnColor;
        int betOnColorId;
        double betOnColorValue;
        Button roulette = findViewById(R.id.buttonRoulette);

        if (Fantiki.currentFantiki >= Fantiki.bet) {
            if (v.getId() == R.id.buttonRed) {
                betOnColorId = R.id.textViewRed;
                Fantiki.betOnRed += Fantiki.bet;
                betOnColorValue = Fantiki.betOnRed;
            } else if (v.getId() == R.id.buttonBlack) {
                betOnColorId = R.id.textViewBlack;
                Fantiki.betOnBlack += Fantiki.bet;
                betOnColorValue = Fantiki.betOnBlack;
            } else {
                betOnColorId = R.id.textViewGreen;
                Fantiki.betOnGreen += Fantiki.bet;
                betOnColorValue = Fantiki.betOnGreen;
            }

            betOnColor = findViewById(betOnColorId);
            betOnColor.setText("" + betOnColorValue + " FAN");

            Fantiki.currentFantiki -= Fantiki.bet;
            Fantiki.ViewFantiki(findViewById(R.id.balanceView));
            roulette.setEnabled(true);
            roulette.setText("Крутить!");
        }
    }

    public void restartBet(View v) {
        Button roulette = findViewById(R.id.buttonRoulette);

        Fantiki.currentFantiki += Fantiki.betOnBlack + Fantiki.betOnRed + Fantiki.betOnGreen;
        clearBetView();
        Fantiki.ZeroBetOnColor();
        Fantiki.ViewFantiki(findViewById(R.id.balanceView));

        roulette.setEnabled(false);
        roulette.setText("Сделайте ставку");
    }

    public void switchEnableButton(boolean fl) {
        findViewById(R.id.navigation_button_roulette).setEnabled(fl);
        findViewById(R.id.buttonBlack).setEnabled(fl);
        findViewById(R.id.buttonRed).setEnabled(fl);
        findViewById(R.id.buttonGreen).setEnabled(fl);
    }

    public void clearBetView() {
        int[] textViewIds = {R.id.textViewRed, R.id.textViewBlack, R.id.textViewGreen};
        for (int textViewId : textViewIds) {
            TextView textView = findViewById(textViewId);
            textView.setText("");
        }
    }

    public void navigationPanel(View v) {
        restartBet(v);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void switchSound(View v) {
        Music.musicSwitch(v);
    }
}
