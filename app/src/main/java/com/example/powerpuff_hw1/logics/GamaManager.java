package com.example.powerpuff_hw1.logics;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.powerpuff_hw1.utilities.CharacterType;

import java.util.Random;

public class GamaManager {

    private final int COOKIE_SCORE = 10;
    private int numOfCollisions = 0;
    private int lives;
    private int cols;
    private int rows;
    private int ppgIndex;
    int score;
    private CharacterType[][] allPositions;


    public GamaManager(int lives, int cols, int rows) {
        if (lives > 0 && lives <= 3) {
            this.lives = lives;
        }
        this.cols = cols;
        this.rows = rows;
        allPositions = new CharacterType[rows][cols];
        resetBoardGame();
    }

    public void decreaseLive() {
        lives--;
        numOfCollisions++;
    }


    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int addedScore) {
        score += addedScore;
    }

    public void resetBoardGame() {
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < cols; j++) {
                allPositions[i][j] = CharacterType.NULL;
            }
        }
        ppgIndex = cols/2;
        score = 0;
        allPositions[rows - 1][ppgIndex] = CharacterType.POWER_PUFF;
    }

    public void restLive() {
        lives += numOfCollisions;
        numOfCollisions = 0;

    }

    public void movePlayer(int direction) {
        if(ppgIndex + direction >= 0 && ppgIndex + direction <= cols - 1) {
            allPositions[rows - 1][ppgIndex] = CharacterType.NULL;
            ppgIndex += direction;
            allPositions[rows - 1][ppgIndex] = CharacterType.POWER_PUFF;
        }
    }

    public int getPpgIndex() { return ppgIndex;}

    public CharacterType[][] getAllPositions(){
        return allPositions;
    }

    public void AddObstacle(){
        double d = new Random().nextDouble();
        CharacterType added;
        if (d > 0.55)
            added = CharacterType.COOKIE;
        else
            added = CharacterType.MOJO_JOJO;
        allPositions[0][getRndInt(cols)] = added;
    }

    public void updateAllObstaclesLocations(boolean addObstacle) {
        for (int i= rows - 2; i>0; i--){
            System.arraycopy(allPositions[i - 1], 0, allPositions[i], 0, cols);
        }

        for (int i = 0 ; i < cols ; i++) {
            allPositions[0][i] = CharacterType.NULL;
        }

        if (addObstacle)
            AddObstacle();
    }

    public boolean isMojoJojoCollision(){
        if(allPositions[rows-2][ppgIndex] == CharacterType.MOJO_JOJO){
            decreaseLive();
            return true;
        }
        return false;
    }

    public boolean isCookieCollision(){
        if(allPositions[rows-2][ppgIndex] == CharacterType.COOKIE){
            addScore(COOKIE_SCORE);
            return true;
        }
        return false;
    }

    public int getRndInt(int range){
        Random random = new Random();
        return random.nextInt(range);
    }

}
