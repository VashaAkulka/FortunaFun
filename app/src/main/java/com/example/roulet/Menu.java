package com.example.roulet;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Menu {

    public static void MenuInit(NavigationView navigation, Context context) {
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Music.musicOFF();

                if (item.getItemId() == R.id.menu_roulette) {
                    intent = new Intent(context, RouletteActivity.class);
                } else if (item.getItemId() == R.id.leader_board) {
                    intent = new Intent(context, LeaderBord.class);
                } else if (item.getItemId() == R.id.home_page) {
                    intent = new Intent(context, HomePage.class);
                } else if (item.getItemId() == R.id.menu_more_less) {
                    intent = new Intent(context, LessMoreActivity.class);
                }

                context.startActivity(intent);
                return true;
            }
        });
    }
}
