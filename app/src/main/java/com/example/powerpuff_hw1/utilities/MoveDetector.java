package com.example.powerpuff_hw1.utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.powerpuff_hw1.interfaces.MoveCallBack;

public class MoveDetector {

    private final int SLOW_DELAY = 1000;
    private final int FAST_DELAY = 350;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private int moveCountX = 0;
    private int moveCountY = 0;
    private long timestamp = 0l;

    private MoveCallBack moveCallBack;

    public MoveDetector(Context context, MoveCallBack moveCallBack) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallBack = moveCallBack;
        initEventListener();
    }

    public int getMoveCountX() {
        return moveCountX;
    }

    public int getMoveCountY() {
        return moveCountY;
    }

    private void initEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                calculateMove(x,y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void calculateMove(float x, float y) {
        int direction = (x > 0) ? -1 : 1;
        int speed = (y > 0) ? FAST_DELAY : SLOW_DELAY;
        if (System.currentTimeMillis() - timestamp > 200){
            timestamp = System.currentTimeMillis();
            if (x > 2.0 || x < -2.0){
                moveCountX++;
                if (moveCallBack != null){
                    moveCallBack.moveX(direction);
                }
            }
            if (y > 2.0 || y < -2.0){
                moveCountY++;
                if (moveCallBack != null){
                    moveCallBack.moveY(speed);
                }
            }
        }
    }


    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
