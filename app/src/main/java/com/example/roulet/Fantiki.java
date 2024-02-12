package com.example.roulet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class Fantiki {
    static double currentFantiki;
    static double bet = 1;
    static double betOnRed = 0;
    static double betOnGreen = 0;
    static double betOnBlack = 0;
    static double win = 0;

    static void loseOrWin_Roulette(String text, View v, Context context) {
        TextView winningView = (TextView)v;
        win = 0;

        switch (text) {
            case "#e8696b": win = betOnRed * 2; break;
            case "#5d6766": win = betOnBlack * 2; break;
            case "#62ae34": win = betOnGreen * 5;
        }

        if (betOnRed == 0 && betOnBlack == 0 && betOnGreen > 0 && win > 0) {
            if (AchievementActivity.achievementAllIn()) AchievementActivity.showMessage(context);
        }
        if (AchievementActivity.achievementUnluck(betOnRed, betOnGreen, betOnBlack, win)) AchievementActivity.showMessage(context);


        winningView.setText("" + win + " FAN");
        currentFantiki += win;
        DataBase.updateData(currentFantiki, context, win, betOnBlack + betOnGreen + betOnRed);
        DataBase.addToHistory(betOnBlack + betOnGreen + betOnRed, win, "Веселый барабан");
    }

    static void ViewFantiki(View v) {
        TextView balanceView = (TextView)v;
        currentFantiki = new BigDecimal(currentFantiki).setScale(2, RoundingMode.HALF_UP).doubleValue();
        balanceView.setText("" + currentFantiki + " FAN");
    }

    static void ViewBet(View v) {
        bet = new BigDecimal(bet).setScale(2, RoundingMode.HALF_UP).doubleValue();
        TextView betView = (TextView)v;
        betView.setText("" + bet);
    }

    static void ZeroBetOnColor() {
        Fantiki.betOnRed = 0;
        Fantiki.betOnBlack = 0;
        Fantiki.betOnGreen = 0;
    }
}
