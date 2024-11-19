package com.raulrh.practicaandroid.util;

import android.content.Context;
import android.media.MediaPlayer;

public class Util {
    public static void playSound(Context context, int soundId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }
}
