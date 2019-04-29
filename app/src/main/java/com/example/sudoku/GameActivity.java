package com.example.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sudoku.com.example.sudoku.game.Cell;
import com.example.sudoku.com.example.sudoku.game.CellCollection;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        try {
            CellCollection collection = CellCollection.createEmptyGrid();
            Cell[][] test2 = collection.getPuzzle(1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
