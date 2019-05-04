package com.example.sudoku.com.example.sudoku.model;

public final class ScoreCounter {
    private int totalscore;

    public int getTotalScore() {
        return totalscore;
    }

    public void addToScore(int score) {
        totalscore += score;
    }

    public void resetScore() {
        totalscore = 0;
    }
}
