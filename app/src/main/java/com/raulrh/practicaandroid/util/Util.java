package com.raulrh.practicaandroid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import com.raulrh.practicaandroid.R;

import java.io.ByteArrayOutputStream;

public class Util {
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public static Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void playSound(Context context, int soundId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }
}
