package com.example.powerpuff_hw1;

import java.util.Random;

public class GamaManager {

    private int lives = 3;
    private final int cols = 3;
    private final int rows = 7;
    private int ppgIndex;
    private CharacterType[][] allCharacters;


    public GamaManager(int lives) {
        if (lives > 0 && lives <= 3)
            this.lives = lives;
        allCharacters = new CharacterType[rows][cols];
        resetGame();
    }

    public void decreaseLive() {
        lives--;
    }

    public int getLives() {
        return lives;
    }


    public void resetGame() {
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < cols; j++) {
                allCharacters[i][j] = CharacterType.EMPTY;
            }
        }
        ppgIndex = cols/2;
        allCharacters[rows - 1][ppgIndex] = CharacterType.POWER_PUFF;
        lives = 3;

    }
    public void movePlayer(int direction) {
        if(ppgIndex + direction >= 0 && ppgIndex + direction <= cols - 1) {
            allCharacters[rows - 1][ppgIndex] = CharacterType.EMPTY;
            ppgIndex += direction;
            allCharacters[rows - 1][ppgIndex] = CharacterType.POWER_PUFF;
        }
       // return ppgIndex;
    }

    public int getPpgIndex() { return ppgIndex;}

    public CharacterType[][] getAllCharacters(){
        return allCharacters;
    }

    public void AddObstacle(){
        allCharacters[0][getRndInt(cols)] = CharacterType.MOJO_JOJO;
    }

    public void updateAllObstaclesLocations(boolean addObstacle) {
        for (int i= rows - 2; i>0; i--){
            System.arraycopy(allCharacters[i - 1], 0, allCharacters[i], 0, cols);
        }

        for (int i = 0 ; i < cols ; i++) {
            allCharacters[0][i] = CharacterType.EMPTY;
        }

        if (addObstacle)
            AddObstacle();
    }

    public boolean isCollision(){
        return allCharacters[rows-2][ppgIndex] == CharacterType.MOJO_JOJO;
    }

    public int getRndInt(int range){
        Random random = new Random();
        return random.nextInt(range);
    }


}
