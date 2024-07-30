package com.example.powerpuff_hw1;

import android.app.Application;

import com.example.powerpuff_hw1.utilities.MyBackgroundMusic;
import com.example.powerpuff_hw1.utilities.MySharedPreference;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySharedPreference.init(this);
        MyBackgroundMusic.init(this);
        MyBackgroundMusic.getInstance().setResourceId(R.raw.snd_background);
    }
}
