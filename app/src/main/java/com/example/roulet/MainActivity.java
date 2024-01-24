package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private int degree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void changeBet(View v) {
        TextView bet = findViewById(R.id.bet);

        if (v.getId() == R.id.buttonMinus) {
            if (Fantiki.bet > 1) Fantiki.bet -= Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;
        }
        else Fantiki.bet += Math.pow(10, Math.floor(Math.log10(Fantiki.bet))) / 2;

        bet.setText("" + Fantiki.bet);
    }

    public void rotateRoulette(View v) {
        degree += (Math.random() * 360) + 1440;
        findViewById(R.id.roulette).animate().rotation(degree).setDuration(4000).withEndAction(new Runnable() {
            @Override
            public void run() {
                int number = (degree % 360) / 40;
                switch (number) {
                    case 0:
                    case 2:
                    case 5:
                    case 7:
                        v.setBackgroundColor(Color.parseColor("#e8696b"));
                        ((Button) v).setText("Red");
                        break;
                    case 1:
                    case 3:
                    case 6:
                    case 8:
                        v.setBackgroundColor(Color.parseColor("#5d6766"));
                        ((Button) v).setText("Black");
                        break;
                    case 4:
                        v.setBackgroundColor(Color.parseColor("#62ae34"));
                        ((Button) v).setText("Green");
                }
            }
        });

    }

    public void navigationPanel(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
