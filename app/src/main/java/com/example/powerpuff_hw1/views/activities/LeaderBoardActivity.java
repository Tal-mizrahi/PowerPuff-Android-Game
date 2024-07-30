package com.example.powerpuff_hw1.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.powerpuff_hw1.R;
import com.example.powerpuff_hw1.interfaces.ListCallBack;
import com.example.powerpuff_hw1.views.fragments.ListFragment;
import com.example.powerpuff_hw1.views.fragments.MapFragment;

public class LeaderBoardActivity extends AppCompatActivity {

    private MapFragment mapFragment;
    private ListFragment listFragment;
    private AppCompatImageView ppg_BTN_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenGame();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mapFragment = new MapFragment();
        listFragment = new ListFragment();

        ppg_BTN_back = findViewById(R.id.ppg_BTN_back);
        ppg_BTN_back.setOnClickListener(v -> moveToHomePage());

        listFragment.setListCallBack(new ListCallBack() {
            @Override
            public void showLocationOnMap(double lat, double lng) {
                mapFragment.focusOnLocation(lat, lng);
            }

        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_LAY_top, listFragment)
                .add(R.id.main_LAY_bottom, mapFragment)
                .commit();
    }

    private void moveToHomePage(){
        Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
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