package com.example.sudoku.com.example.sudoku.model;

public final class ScoreCounter {
    private int totalScore;

    int getTotalScore() {
        return totalScore;
    }

    public void addToScore(int score) {
        totalScore += score;
    }

    void resetScore() {
        totalScore = 0;
    }
}
