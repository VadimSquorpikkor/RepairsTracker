package com.atomtex.repairstracker;

import android.app.Activity;

import static com.atomtex.repairstracker.SaveLoad.loadIntParam;
import static com.atomtex.repairstracker.SaveLoad.saveParam;

class ThemeUtils {
    private static int sTheme;
    public final static int THEME_LIGHT = 1;
    public final static int THEME_DARK = 2;
    public final static String KEY_THEME = "theme";

    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        saveParam(KEY_THEME, sTheme);
        activity.recreate();
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        sTheme = loadIntParam(KEY_THEME);
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

}
