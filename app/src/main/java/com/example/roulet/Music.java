package com.example.roulet;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

public class Music {
    static MediaPlayer music;
    static int[] imageIds = {R.drawable.icon_without_sound, R.drawable.icon_with_sound};
    static int currentImageIndex = 0;

    public static void musicInit(Context context, int uri) {
        music = MediaPlayer.create(context, uri);
        music.setLooping(true);
        music.start();
    }

    public static void musicSwitch(View v) {

        if (music.isPlaying()) music.pause();
        else music.start();

        ImageView imageView = (ImageView) v;
        imageView.setImageResource(imageIds[currentImageIndex]);
        currentImageIndex = (currentImageIndex + 1) % imageIds.length;
    }

    public static void musicOFF() {
        music.stop();
        currentImageIndex = 0;
    }
}
