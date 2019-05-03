package com.example.sudoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.sudoku.view.buttonsGrid.ButtonsGridView;
import com.example.sudoku.view.commands.ChangeValueCommand;
import com.example.sudoku.view.sudokuGrid.SudokuGridView;
import com.example.sudoku.viewModel.SudokuPuzzleViewModel;

public class SolveActivity extends AppCompatActivity implements View.OnClickListener {

    private SudokuPuzzleViewModel viewModel;
    private SudokuGridView gridView;
    private ButtonsGridView buttonView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);
        viewModel = new SudokuPuzzleViewModel();
        gridView = findViewById(R.id.sudokuGridView);
        buttonView = findViewById(R.id.buttonsgridview);
        buttonView.initialize(new ChangeValueCommand(gridView));

        Button solveButton = findViewById(R.id.solve_grid_Button);
        solveButton.setOnClickListener(this);

        gridView.initialize(viewModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.solve_grid_Button:
                if (viewModel.canPuzzleBeSolved())
                    gridView.solve();
                else
                    //TODO: add information about wrong values
                break;
            default:
                break;
        }
    }
}
