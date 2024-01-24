package com.example.roulet;

import android.view.View;
import android.widget.TextView;


public class Fantiki {
    static double currentFantiki = 100;
    static double bet = 1;
    static double betOnRed = 0;
    static double betOnGreen = 0;
    static double betOnBlack = 0;

    static void loseOrWin(String text, View v) {
        TextView winningView = (TextView)v;
        double win = 0;

        switch (text) {
            case "#e8696b": win = betOnRed * 2; break;
            case "#5d6766": win = betOnBlack * 2; break;
            case "#62ae34": win = betOnGreen * 5;
        }

        winningView.setText("" + win + " FAN");
        currentFantiki += win;
    }

    static void ViewFantiki(View v) {
        TextView balanceView = (TextView)v;
        balanceView.setText("" + Fantiki.currentFantiki + " FAN");
    }

    static void ViewBet(View v) {
        TextView betView = (TextView)v;
        betView.setText("" + Fantiki.bet);
    }

    static void ZeroBetOnColor() {
        Fantiki.betOnRed = 0;
        Fantiki.betOnBlack = 0;
        Fantiki.betOnGreen = 0;
    }
}
