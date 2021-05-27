package com.atomtex.repairstracker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import static com.atomtex.repairstracker.Constant.SWITCHER_STATE;

public class MainViewModel extends AndroidViewModel {

    private final FireDBHelper dbh;

    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
        dbh = new FireDBHelper();
    }






}
