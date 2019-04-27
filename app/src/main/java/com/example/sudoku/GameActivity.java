package com.example.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sudoku.com.example.sudoku.game.SudokuGenerator;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SudokuGenerator test = new SudokuGenerator();
        try {
            int[] val = test.getPuzzle(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
