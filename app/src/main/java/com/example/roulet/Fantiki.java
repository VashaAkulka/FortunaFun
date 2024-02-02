package com.example.roulet;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;


public class Fantiki {
    static double currentFantiki;
    static double bet = 1;
    static double betOnRed = 0;
    static double betOnGreen = 0;
    static double betOnBlack = 0;
    static double win = 0;

    static void loseOrWin_Roulette(String text, View v) {
        TextView winningView = (TextView)v;
        win = 0;

        switch (text) {
            case "#e8696b": win = betOnRed * 2; break;
            case "#5d6766": win = betOnBlack * 2; break;
            case "#62ae34": win = betOnGreen * 5;
        }

        winningView.setText("" + win + " FAN");
        currentFantiki += win;
        DataBase.updateData(currentFantiki);
    }

    static void ViewFantiki(View v) {
        TextView balanceView = (TextView)v;
        balanceView.setText("" + currentFantiki + " FAN");
    }

    static void ViewBet(View v) {
        TextView betView = (TextView)v;
        betView.setText("" + bet);
    }

    static void ZeroBetOnColor() {
        Fantiki.betOnRed = 0;
        Fantiki.betOnBlack = 0;
        Fantiki.betOnGreen = 0;
    }
}
