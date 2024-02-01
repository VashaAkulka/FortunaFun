package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Menu.MenuInit(findViewById(R.id.navigation_home_page), this);
        Fantiki.ViewFantiki(findViewById(R.id.balanceView_home_page));
    }

    public void navigationPanelHomePage(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_leader_bord);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}