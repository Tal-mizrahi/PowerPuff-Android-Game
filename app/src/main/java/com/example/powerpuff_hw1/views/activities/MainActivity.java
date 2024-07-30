package com.example.powerpuff_hw1.views.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.powerpuff_hw1.utilities.CharacterType;
import com.example.powerpuff_hw1.interfaces.MoveCallBack;
import com.example.powerpuff_hw1.logics.GamaManager;
import com.example.powerpuff_hw1.models.GameObject;
import com.example.powerpuff_hw1.R;
import com.example.powerpuff_hw1.models.Player;
import com.example.powerpuff_hw1.utilities.MoveDetector;
import com.example.powerpuff_hw1.utilities.MySharedPreference;
import com.example.powerpuff_hw1.utilities.SoundPlayer;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    private final int ROWS = 7;
    private final int COLS = 5;
    private final int LIVES = 3;

    private static final double DEFAULT_LATITUDE_NO_PERMISSION = 999.0;
    private static final double DEFAULT_LONGITUDE_NO_PERMISSION = 999.0;
    private FusedLocationProviderClient fusedLocationClient;


    private AppCompatImageView[][] gameBoard;
    private AppCompatImageView[] ppg_IMG_hearts;
    private FloatingActionButton ppg_BTN_left;
    private FloatingActionButton ppg_BTN_right;
    private AppCompatEditText ppg_EDTXT_name;
    private ExtendedFloatingActionButton ppg_BTN_ok;
    private GamaManager gameManager;
    private MoveDetector moveDetector;
    private GameObject powerPuff;
    private SoundPlayer soundPlayer;
    private GameObject mojoJojo;
    private GameObject cookie;
    private MaterialTextView ppg_LBL_score;
    private MaterialTextView ppg_LBL_the_score;
    private int delay = 0;
    private CardView ppg_CRD_GAMEOVER;
    private  boolean addObstacle = false;
    private final Handler handler = new Handler();
    private final int VIBRATE_DURATION = 300;
    private boolean isGameOver = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenGame();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        gameManager = new GamaManager(LIVES, COLS, ROWS);
        updatePowerPuffUI();
        gameManager.resetBoardGame();
        soundPlayer = new SoundPlayer(this);
        ppg_CRD_GAMEOVER.setVisibility(View.INVISIBLE);
        ppg_BTN_left.setOnClickListener(v -> movePPG(-1));
        ppg_BTN_right.setOnClickListener(v -> movePPG(1));
        ppg_BTN_ok.setOnClickListener(v -> moveToLeaderBoard());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startGame();

    }


    private void initViews() {
        Intent prev = getIntent();
        delay = prev.getExtras().getInt("DELAY");
        if (!prev.getExtras().getBoolean("IS_BUTTONS")) {
            initMoveDetector();
            ppg_BTN_left.setVisibility(View.INVISIBLE);
            ppg_BTN_right.setVisibility(View.INVISIBLE);
        }
    }

    private void initMoveDetector() {
        moveDetector = new MoveDetector(this,
                new MoveCallBack() {

                    @Override
                    public void moveX(int direction) {
                        movePPG(direction);
                    }

                    @Override
                    public void moveY(int speed) {
                        delay = speed;
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!isGameOver)
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

    @Override
    protected void onPause() {
        super.onPause();
        if(moveDetector != null)
            moveDetector.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(moveDetector != null && !isGameOver)
            moveDetector.start();
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, delay);
            gameContinuation();

        }
    };

    private void stopGame() {
        handler.removeCallbacks(runnable);
    }

    private void startGame() {
        handler.postDelayed(runnable, delay);
    }


    public void gameContinuation() {
        checkCollision();
        gameManager.updateAllObstaclesLocations(addObstacle);
        addObstacle = !addObstacle;
        updateObstaclesUI();

    }

    public void checkCollision() {
        if(gameManager.isMojoJojoCollision()) {
            updateLivesUI();
            vibrate();
            soundPlayer.playSound(R.raw.snd_ouch);
            if(gameManager.getLives() <= 0) {
                loseGame();
            }
//           I prefer that only sound is played without a toast message
//                createToast("You hit Mojo-Jojo");
        }
        else if (gameManager.isCookieCollision()) {
            updateScoreUI();
            soundPlayer.playSound(R.raw.snd_bonus_points);

        }
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
        if(!isGameOver){
            gameManager.movePlayer(direction);
            updatePowerPuffUI();
        }
    }

    public void updateScoreUI(){
        ppg_LBL_score.setText(String.valueOf(gameManager.getScore()));
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
        for (AppCompatImageView ppgImgHeart : ppg_IMG_hearts) {
            ppgImgHeart.setVisibility(View.VISIBLE);
        }

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
                } else if (locationMatrix[i][j] == CharacterType.COOKIE) {
                    gameBoard[i][j].setImageResource(cookie.getImage());
                }
            }
        }
    }

    public void loseGame() {
        isGameOver = true;
        stopGame();
        ppg_LBL_the_score.setText(String.valueOf(gameManager.getScore()));
        ppg_CRD_GAMEOVER.setVisibility(View.VISIBLE);
    }

    private void moveToLeaderBoard() {
        String name = ppg_EDTXT_name.getText().toString();
        if (name == null || name.isBlank()) {
            createToast("Please enter your name");
        } else {
            Player player = new Player();
            player.setName(name);
            player.setScore(gameManager.getScore());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                player.setLat(DEFAULT_LATITUDE_NO_PERMISSION);
                player.setLng(DEFAULT_LONGITUDE_NO_PERMISSION);
            } else {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        player.setLat(location.getLatitude());
                        player.setLng(location.getLongitude());
                    }
                });
            }
            MySharedPreference.getInstance().addPlayer(player);
            MySharedPreference.getInstance().saveAllPlayers("PLAYERS");
            Intent intent = new Intent(getApplicationContext(), LeaderBoardActivity.class);
            startActivity(intent);
        }
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

    private void findViews() {
        ppg_BTN_left = findViewById(R.id.ppg_BTN_left);
        ppg_BTN_right = findViewById(R.id.ppg_BTN_right);
        ppg_LBL_score = findViewById(R.id.ppg_LBL_score);
        ppg_LBL_the_score = findViewById(R.id.ppg_LBL_the_score);
        ppg_EDTXT_name = findViewById(R.id.ppg_EDTXT_name);
        ppg_BTN_ok = findViewById(R.id.ppg_BTN_ok);
        ppg_CRD_GAMEOVER = findViewById(R.id.ppg_CRD_GAMEOVER);
        powerPuff = new GameObject().setImage(R.drawable.ic_ppg);
        mojoJojo = new GameObject().setImage(R.drawable.ic_mojo_jojo);
        cookie = new GameObject().setImage(R.drawable.ic_cotton_candy);


        gameBoard = new AppCompatImageView[][] {
                {findViewById(R.id.ppg_IMG_MAT_00), findViewById(R.id.ppg_IMG_MAT_01), findViewById(R.id.ppg_IMG_MAT_02), findViewById(R.id.ppg_IMG_MAT_03), findViewById(R.id.ppg_IMG_MAT_04)},
                {findViewById(R.id.ppg_IMG_MAT_10), findViewById(R.id.ppg_IMG_MAT_11), findViewById(R.id.ppg_IMG_MAT_12), findViewById(R.id.ppg_IMG_MAT_13), findViewById(R.id.ppg_IMG_MAT_14)},
                {findViewById(R.id.ppg_IMG_MAT_20), findViewById(R.id.ppg_IMG_MAT_21), findViewById(R.id.ppg_IMG_MAT_22), findViewById(R.id.ppg_IMG_MAT_23), findViewById(R.id.ppg_IMG_MAT_24)},
                {findViewById(R.id.ppg_IMG_MAT_30), findViewById(R.id.ppg_IMG_MAT_31), findViewById(R.id.ppg_IMG_MAT_32), findViewById(R.id.ppg_IMG_MAT_33), findViewById(R.id.ppg_IMG_MAT_34)},
                {findViewById(R.id.ppg_IMG_MAT_40), findViewById(R.id.ppg_IMG_MAT_41), findViewById(R.id.ppg_IMG_MAT_42), findViewById(R.id.ppg_IMG_MAT_43), findViewById(R.id.ppg_IMG_MAT_44)},
                {findViewById(R.id.ppg_IMG_MAT_50), findViewById(R.id.ppg_IMG_MAT_51), findViewById(R.id.ppg_IMG_MAT_52), findViewById(R.id.ppg_IMG_MAT_53), findViewById(R.id.ppg_IMG_MAT_54)},
                {findViewById(R.id.ppg_IMG_MAT_60), findViewById(R.id.ppg_IMG_MAT_61), findViewById(R.id.ppg_IMG_MAT_62), findViewById(R.id.ppg_IMG_MAT_63), findViewById(R.id.ppg_IMG_MAT_64)}
                };


        ppg_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.ppg_IMG_heart1),
                findViewById(R.id.ppg_IMG_heart2),
                findViewById(R.id.ppg_IMG_heart3)
        };


    }

}
