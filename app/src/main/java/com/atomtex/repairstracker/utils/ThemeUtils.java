package com.atomtex.repairstracker.utils;

import android.app.Activity;

import static com.atomtex.repairstracker.utils.SaveLoad.loadInt;
import static com.atomtex.repairstracker.utils.SaveLoad.save;

import com.atomtex.repairstracker.R;

public class ThemeUtils {
    private static int sTheme;
    public final static int THEME_LIGHT = 1;
    public final static int THEME_DARK = 2;
    public final static String KEY_THEME = "theme";

    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        save(KEY_THEME, sTheme);
        activity.recreate();
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        sTheme = loadInt(KEY_THEME);
        switch (sTheme)
        {
            default:
            case THEME_DARK: activity.setTheme(R.style.Theme_Leonardo_Dark); break;
            case THEME_LIGHT: activity.setTheme(R.style.Theme_Leonardo); break;
        }
    }

    public static int getTheme() {
        return sTheme;
    }

}
