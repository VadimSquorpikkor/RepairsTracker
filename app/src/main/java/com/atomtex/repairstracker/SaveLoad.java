package com.atomtex.repairstracker;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

class SaveLoad {
    /**Сохранение по ключу*/
    public static void saveParam(String key, int param) {
        SharedPreferences mPreferences = App.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        mPreferences.edit().putInt(key, param).apply();
    }

    /**Загрузка int по ключу*/
    public static int loadIntParam(String key) {
        SharedPreferences mPreferences = App.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        int value = 2;
        if (mPreferences.contains(key)) {
            value = mPreferences.getInt(key, 2);
        }
        return value;
    }

    /**Сохранение массива по ключу*/
    public static void saveArray(String key, ArrayList<String> list) {
        SharedPreferences mPreferences = App.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        //очистить. если не очищать, то в случае, когда размер массива меньше сохраненного ранее, будет оставаться "хвост" предыдущего массива
        int count = 0;
        while (mPreferences.contains(key + count)) {
            editor.remove(key + count);
            count++;
        }

        for (int i = 0; i < list.size(); i++) {
            editor.putString(key + i, list.get(i));
        }
        editor.apply();
    }

    /**Загрузка ArrayList<String> по ключу*/
    public static ArrayList<String> loadStringArray(String key) {
        SharedPreferences mPreferences = App.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        ArrayList<String> list = new ArrayList<>();
        int count = 0;
        while (mPreferences.contains(key + count)) {
            String s = mPreferences.getString(key + count, "");
            list.add(s);
            count++;
        }
        return list;
    }
}
