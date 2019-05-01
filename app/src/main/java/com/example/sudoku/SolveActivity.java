package com.example.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudoku.view.GameEngine;

public class SolveActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);

        Button solveButton = findViewById(R.id.solve_grid_Button);
        solveButton.setOnClickListener(this);

        GameEngine.getInstance().createEmptyGrid(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.solve_grid_Button:
                GameEngine.getInstance().solve();
                break;
            default:
                break;
        }
    }
}
