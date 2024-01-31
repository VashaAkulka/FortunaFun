package com.example.roulet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

public class LeaderBord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_bord);

        ListView leaderBoard = findViewById(R.id.leader_board);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), DataBase.GetUserData());
        leaderBoard.setAdapter(customBaseAdapter);

        Menu.MenuInit(findViewById(R.id.navigation_leader_board), this);
    }

    public void navigationPanelLeaderBord(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_leader_bord);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}