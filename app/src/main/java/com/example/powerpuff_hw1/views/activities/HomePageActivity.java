package com.example.powerpuff_hw1.views.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.powerpuff_hw1.R;
import com.example.powerpuff_hw1.utilities.MyBackgroundMusic;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class HomePageActivity extends AppCompatActivity {

    private final String BUTTONS = "Mode: Buttons";
    private final String SENSORS = "Mode: Sensors";
    private final int SLOW_DELAY = 1000;
    private final int FAST_DELAY = 350;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private ExtendedFloatingActionButton ppg_BTN_changeMode;
    private ExtendedFloatingActionButton ppg_BTN_records;
    private ExtendedFloatingActionButton ppg_BTN_fast;
    private ExtendedFloatingActionButton ppg_BTN_slow;
    private MaterialTextView ppg_LBL_mode;

    private boolean isButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenGame();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        findViews();
        ppg_BTN_changeMode.setOnClickListener(v -> changeMode());
        ppg_BTN_fast.setOnClickListener(v -> moveToGame(FAST_DELAY));
        ppg_BTN_slow.setOnClickListener(v -> moveToGame(SLOW_DELAY));
        ppg_BTN_records.setOnClickListener(v -> moveToLeaderBoard());
        isButtons = true;
        requestLocationPermission();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MyBackgroundMusic.getInstance().pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyBackgroundMusic.getInstance().playMusic();
    }



    public void changeMode() {
        isButtons = !isButtons;
        if (isButtons) {
            ppg_LBL_mode.setText(BUTTONS);
        } else {
            ppg_LBL_mode.setText(SENSORS);
        }
    }

    private void moveToGame(int delay) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_BUTTONS", isButtons);
        bundle.putInt("DELAY", delay);
        i.putExtras(bundle);
        MyBackgroundMusic.getInstance().stopMusic();
        startActivity(i);
        finish();
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void findViews() {
        ppg_BTN_changeMode = findViewById(R.id.ppg_BTN_changeMode);
        ppg_BTN_records = findViewById(R.id.ppg_BTN_records);
        ppg_BTN_fast = findViewById(R.id.ppg_BTN_fast);
        ppg_BTN_slow = findViewById(R.id.ppg_BTN_slow);
        ppg_LBL_mode = findViewById(R.id.ppg_LBL_mode);
    }

    private void moveToLeaderBoard() {
        Intent i = new Intent(getApplicationContext(), LeaderBoardActivity.class);
        MyBackgroundMusic.getInstance().stopMusic();
        startActivity(i);
        finish();
    }

    public void fullScreenGame() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

}