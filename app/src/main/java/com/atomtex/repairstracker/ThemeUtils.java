package com.atomtex.repairstracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

class ThemeUtils {
    private static int sTheme;
    public final static int THEME_LIGHT = 1;
    public final static int THEME_DARK = 2;
    public final static String KEY_THEME = "theme";
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        saveParam(KEY_THEME, sTheme, activity);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        sTheme = loadTheme(activity);
        switch (sTheme)
        {
            default:
            case THEME_DARK: activity.setTheme(R.style.Theme_Leonardo_Dark); break;
            case THEME_LIGHT: activity.setTheme(R.style.Theme_Leonardo); break;
        }
    }

    static int getTheme() {
        return sTheme;
    }

    static int loadTheme(Activity activity) {
        SharedPreferences mPreferences;
        mPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        int value = 2;
        if (mPreferences.contains(KEY_THEME)) {
            value = mPreferences.getInt(KEY_THEME, 2);
        }
        return value;
    }

    static void saveParam(String key, int param, Activity activity) {
        SharedPreferences mPreferences;
        mPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        mPreferences.edit().putInt(key, param).apply();
    }
}
