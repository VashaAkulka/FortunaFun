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
                } else if (item.getItemId() == R.id.achievements) {
                    intent = new Intent(context, AchievementActivity.class);
                } else if (item.getItemId() == R.id.profile) {
                    intent = new Intent(context, ProfileActivity.class);
                } else if (item.getItemId() == R.id.exit) {
                    PreferenceKey.removeKey(context);
                    intent = new Intent(context, LoginActivity.class);
                } else if (item.getItemId() == R.id.coinflip) {
                    intent = new Intent(context, MainActivity.class);
                } else if (item.getItemId() == R.id.baraban) {
                    intent = new Intent(context, Baraban.class);
                } else if (item.getItemId() == R.id.miner) {
                    intent = new Intent(context, Miner.class);
                } else if (item.getItemId() == R.id.fly) {
                    intent = new Intent(context, FlyPlane.class);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                return true;
            }
        });
    }
}
