package com.example.powerpuff_hw1;

import java.util.Random;

public class GamaManager {

    private int numOfCollisions = 0;
    private int lives;
    private int cols;
    private int rows;
    private int ppgIndex;
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


    public void resetBoardGame() {
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < cols; j++) {
                allPositions[i][j] = CharacterType.NULL;
            }
        }
        ppgIndex = cols/2;
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
        allPositions[0][getRndInt(cols)] = CharacterType.MOJO_JOJO;
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

    public boolean isCollision(){
        return allPositions[rows-2][ppgIndex] == CharacterType.MOJO_JOJO;
    }

    public int getRndInt(int range){
        Random random = new Random();
        return random.nextInt(range);
    }


}
