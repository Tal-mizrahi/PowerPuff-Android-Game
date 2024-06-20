package com.example.powerpuff_hw1;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private final int ROWS = 7;
    private final int COLS = 3;
    private final int LIVES = 3;
    private AppCompatImageView[][] gameBoard;
    private AppCompatImageView[] ppg_IMG_hearts;
    private FloatingActionButton ppg_BTN_left;
    private FloatingActionButton ppg_BTN_right;
    private GamaManager gameManager;
    private GameObject powerPuff;
    private GameObject mojoJojo;
    private  boolean addObstacle = false;
    private final Handler handler = new Handler();
    private final int DELAY = 1000;
    private final int VIBRATE_DURATION = 300;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViews();
        gameManager = new GamaManager(LIVES, COLS, ROWS);
        gameManager.resetBoardGame();
        ppg_BTN_left.setOnClickListener(v -> movePPG(-1));
        ppg_BTN_right.setOnClickListener(v -> movePPG(1));
        startGame();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGame();
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, DELAY);
            gameContinuation();

        }
    };

    private void stopGame() {
        handler.removeCallbacks(runnable);
    }

    private void startGame() {
        handler.postDelayed(runnable, DELAY);
    }


    public void gameContinuation() {
        if(gameManager.isCollision()){
            gameManager.decreaseLive();
            updateLivesUI();
            vibrate();
            if(gameManager.getLives() > 0)
                createToast("You hit Mojo-Jojo");
            else
               loseGame();
        }

        gameManager.updateAllObstaclesLocations(addObstacle);
        addObstacle = !addObstacle;
        updateObstaclesUI();

    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for VIBRATE_DURATION milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(VIBRATE_DURATION);
        }
    }

    public void createToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void movePPG(int direction) {
        gameManager.movePlayer(direction);
        updatePowerPuffUI();
    }

    public void updatePowerPuffUI(){
        CharacterType[] ppgRow = gameManager.getAllPositions()[ROWS - 1];

        for(int i = 0 ; i < COLS ; i++) {
            if(ppgRow[i] == CharacterType.NULL)
                gameBoard[ROWS - 1][i].setImageResource(0);
            else if(ppgRow[i] == CharacterType.POWER_PUFF)
                gameBoard[ROWS - 1][i].setImageResource(powerPuff.getImage());
        }
    }

    private void updateLivesUI() {
        int SZ = ppg_IMG_hearts.length;
        Log.d("pttt", "SZ" + SZ);

        for (int i = 0; i < SZ; i++) {
            ppg_IMG_hearts[i].setVisibility(View.VISIBLE);
        }

        int num = SZ-gameManager.getLives();
        Log.d("pttt", "SZ - gameManager.getLives() " + num);

        for (int i = 0; i < SZ - gameManager.getLives(); i++) {
            ppg_IMG_hearts[i].setVisibility(View.INVISIBLE);
        }
    }

    public void updateObstaclesUI() {
       CharacterType[][] locationMatrix = gameManager.getAllPositions();
        for(int i = 0 ; i < ROWS - 1 ; i++) {
            for (int j = 0; j < COLS; j++) {
                if (locationMatrix[i][j] == CharacterType.NULL) {
                    gameBoard[i][j].setImageResource(0);
                } else if (locationMatrix[i][j] == CharacterType.MOJO_JOJO) {
                    gameBoard[i][j].setImageResource(mojoJojo.getImage());
                }
            }
        }
    }

    public void loseGame() {
        createToast("You lose!");
        stopGame();
        gameManager.resetBoardGame();
        gameManager.restLive();
        updatePowerPuffUI();
        updateLivesUI();
        updateObstaclesUI();
        addObstacle = false;
        gameBoard[ROWS - 1][gameManager.getPpgIndex()].setImageResource(powerPuff.getImage());
        startGame();
    }


    private void findViews() {

        ppg_BTN_left = findViewById(R.id.ppg_BTN_left);
        ppg_BTN_right = findViewById(R.id.ppg_BTN_right);
        powerPuff = new GameObject().setImage(R.drawable.ic_ppg);
        mojoJojo = new GameObject().setImage(R.drawable.ic_mojo_jojo);

        gameBoard = new AppCompatImageView[][] {
                {findViewById(R.id.ppg_REL_MAT_00), findViewById(R.id.ppg_REL_MAT_01), findViewById(R.id.ppg_REL_MAT_02)},
                {findViewById(R.id.ppg_REL_MAT_10), findViewById(R.id.ppg_REL_MAT_11), findViewById(R.id.ppg_REL_MAT_12)},
                {findViewById(R.id.ppg_REL_MAT_20), findViewById(R.id.ppg_REL_MAT_21), findViewById(R.id.ppg_REL_MAT_22)},
                {findViewById(R.id.ppg_REL_MAT_30), findViewById(R.id.ppg_REL_MAT_31), findViewById(R.id.ppg_REL_MAT_32)},
                {findViewById(R.id.ppg_REL_MAT_40), findViewById(R.id.ppg_REL_MAT_41), findViewById(R.id.ppg_REL_MAT_42)},
                {findViewById(R.id.ppg_REL_MAT_50), findViewById(R.id.ppg_REL_MAT_51), findViewById(R.id.ppg_REL_MAT_52)},
                {findViewById(R.id.ppg_REL_MAT_60), findViewById(R.id.ppg_REL_MAT_61), findViewById(R.id.ppg_REL_MAT_62)}

        };

        ppg_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.ppg_IMG_heart1),
                findViewById(R.id.ppg_IMG_heart2),
                findViewById(R.id.ppg_IMG_heart3)
        };


    }




}