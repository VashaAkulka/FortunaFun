package com.example.roulet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DataBase.setText(findViewById(R.id.drawerLayout_profile));

        String strUri = DataBase.takeAvatar();
        if (strUri != null) {
            Uri uri = Uri.parse(strUri);
            Glide.with(this)
                    .load(uri)
                    .into((ImageView)findViewById(R.id.profile_avatar));
        }

        ListView profileList = findViewById(R.id.profile_list);

        CustomProfileAdapter customProfileAdapter = new CustomProfileAdapter(this);
        profileList.setAdapter(customProfileAdapter);

        findViewById(R.id.profile_trash).setOnClickListener(view -> {
            DataBase.deleteUserHistory();
            customProfileAdapter.notifyDataSetChanged();
        });

        Menu.MenuInit(findViewById(R.id.navigation_profile), this);
    }

    public void navigationPanelProfile(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout_profile);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void SetImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeImage.launch(intent);
    }

    ActivityResultLauncher<Intent> takeImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    DataBase.editAvatar(uri.toString());
                    Glide.with(this)
                            .load(uri)
                            .into((ImageView)findViewById(R.id.profile_avatar));
                }
            }
    );
}