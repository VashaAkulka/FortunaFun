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

    }

    public void TakeBonus(View v) {
        Fantiki.currentFantiki += 50;
        DataBase.updateData(Fantiki.currentFantiki);
        Fantiki.ViewFantiki(findViewById(R.id.balanceView_home_page));
        findViewById(R.id.take_bonus_button).setEnabled(false);
    }
}