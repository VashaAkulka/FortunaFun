package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class RouletteActivity extends AppCompatActivity {

    private int degree = 0;
    MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);


        Fantiki.ViewFantiki(findViewById(R.id.balanceView));
        Fantiki.ViewBet(findViewById(R.id.betView));
        music = MediaPlayer.create(this, R.raw.music);
        music.setLooping(true);
        music.start();
    }

    public void changeBet(View v) {
        if (v.getId() == R.id.buttonMinus) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
        }
        else Fantiki.bet += Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;

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

            Fantiki.loseOrWin(color, findViewById(R.id.winningView));
            Fantiki.ViewFantiki(findViewById(R.id.balanceView));

            v.setBackgroundColor(Color.parseColor(color));
            ((Button) v).setText("Сделайте ставку");

            clearBetView();
            Fantiki.ZeroBetOnColor();
            switchEnableButton(true);
            findViewById(R.id.restartBet).setEnabled(true);

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
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }




    int[] imageIds = {R.drawable.icon_without_sound, R.drawable.icon_with_sound};
    int currentImageIndex = 0;

    public void switchSound(View v) {
        if (music.isPlaying()) music.pause();
        else music.start();

        ImageView imageView = (ImageView) v;
        imageView.setImageResource(imageIds[currentImageIndex]);
        currentImageIndex = (currentImageIndex + 1) % imageIds.length;
    }
}
