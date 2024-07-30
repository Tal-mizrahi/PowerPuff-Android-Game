package com.example.powerpuff_hw1.models;

public class Cookie extends GameObject{

    int score;

    public Cookie() {
        super();
    }

    public int getScore() {
        return score;
    }

    public Cookie setScore(int score) {
        this.score = score;
        return this;
    }
}
