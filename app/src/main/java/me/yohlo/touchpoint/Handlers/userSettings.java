package me.yohlo.touchpoint.Handlers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yohlo on 9/9/16.
 */

// I want this class to basically be my shared preferences handler.
public class userSettings {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String HIGH = "highscore", RED = "maxred", TOTAL = "total";
    private Context context;

    public userSettings(Context context) { this.context = context; }

    private SharedPreferences getPrefs(String PREF_NAME) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public int getHighScore() {
        return context.getSharedPreferences(HIGH, Context.MODE_PRIVATE).getInt(HIGH, 0);
    }
    public void setHighScore(int high) {
        SharedPreferences.Editor editor = getPrefs(HIGH).edit();
        editor.putInt(HIGH, high);
        editor.commit();
    }
    public int getMaxRed() {
        return context.getSharedPreferences(RED, Context.MODE_PRIVATE).getInt(RED, 6);
    }

    public int getTotal() {
        return context.getSharedPreferences(TOTAL, Context.MODE_PRIVATE).getInt(TOTAL, 0);
    }
    public void setTotal(int total) {
        SharedPreferences.Editor editor = getPrefs(TOTAL).edit();
        editor.putInt(TOTAL, total);
        editor.commit();
    }

}