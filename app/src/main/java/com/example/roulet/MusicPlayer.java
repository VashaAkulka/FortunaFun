package com.example.roulet;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {

    private static MediaPlayer mediaPlayer;

    public static void playMusic(Context context, int resourceId, boolean loop) {
        stopMusic();

        mediaPlayer = MediaPlayer.create(context, resourceId);

        if (loop) {
            mediaPlayer.setLooping(true); // Если нужно зациклить
        } else {
            // Если не зацикливаем, добавляем слушатель для обработки окончания воспроизведения
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopMusic(); // Останавливаем после завершения воспроизведения
                }
            });
        }

        mediaPlayer.start();
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
