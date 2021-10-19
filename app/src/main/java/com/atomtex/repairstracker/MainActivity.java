package com.atomtex.repairstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.atomtex.repairstracker.fragments.MainFragment;
import com.atomtex.repairstracker.utils.ThemeUtils;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_MEMORY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);

        Log.e("TAG", "-------------------------------");
        Log.e("TAG", "♣" + BuildConfig.APPLICATION_ID);
        Log.e("TAG", "♣VERSION_NAME: " + BuildConfig.VERSION_NAME);
        Log.e("TAG", "-------------------------------");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, MainFragment.newInstance())
                    .commitNow();
        }
        check();
    }


    private void check() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_MEMORY);
        } else Log.e("TAG", "already had been granted");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_MEMORY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "granted");
            }
        }
    }
}