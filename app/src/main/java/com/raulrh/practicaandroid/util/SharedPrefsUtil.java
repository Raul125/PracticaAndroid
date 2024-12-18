package com.raulrh.practicaandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtil {

    public static final String PREF_KEY_ROWS = "minesweeper_rows";
    public static final String PREF_KEY_COLS = "minesweeper_cols";
    public static final String PREF_KEY_MINES = "minesweeper_mines";
    private static final String PREFS_NAME = "app_prefs";

    public static void putInt(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultValue);
    }
}