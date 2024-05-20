package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Menu.MenuInit(findViewById(R.id.navigation_home_page), this);
        Fantiki.ViewFantiki(findViewById(R.id.balanceView_home_page));

        if (Fantiki.currentFantiki > 1) findViewById(R.id.take_bonus_button).setEnabled(false);
    }

    public void navigationPanelHomePage(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_home_page);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void HappyWheel(View v) {
        Intent intent = new Intent(this, RouletteActivity.class);
        startActivity(intent);
    }

    public void MoreLess(View v) {
        Intent intent = new Intent(this, LessMoreActivity.class);
        startActivity(intent);
    }

    public void Baraban(View v) {
        Intent intent = new Intent(this, Baraban.class);
        startActivity(intent);
    }

    public void Coins(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void FlyPlain(View v) {
        Intent intent = new Intent(this, FlyPlane.class);
        startActivity(intent);
    }
    public void Miner(View v) {
        Intent intent = new Intent(this, Miner.class);
        startActivity(intent);
    }

    public void Percent(View v) {
        Intent intent = new Intent(this, Percent.class);
        startActivity(intent);
    }

    public void Racing(View v){
        Intent intent = new Intent(this, Racing.class);
        startActivity(intent);
    }

    public void TakeBonus(View v) {
        Fantiki.currentFantiki += 50;
        DataBase.updateData(Fantiki.currentFantiki, this, 0, 0);
        Fantiki.ViewFantiki(findViewById(R.id.balanceView_home_page));
        findViewById(R.id.take_bonus_button).setEnabled(false);
        if (AchievementActivity.achievementBonus()) AchievementActivity.showMessage(this);
    }
}