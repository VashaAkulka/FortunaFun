package com.example.roulet;

public class Fantiki {
    static double currentFantiki = 100;
    static double bet = 1;

    static void loseOrWin(boolean result){
        if (result) currentFantiki =- bet;
        else currentFantiki += bet;
    }
}
