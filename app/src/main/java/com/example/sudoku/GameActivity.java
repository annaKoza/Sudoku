package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudoku.view.buttonsGrid.ButtonsGridView;
import com.example.sudoku.view.commands.ChangeValueCommand;
import com.example.sudoku.view.sudokuGrid.SudokuGridView;
import com.example.sudoku.viewModel.SudokuPuzzleViewModel;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private SudokuPuzzleViewModel viewModel;
    private View layout;
    private SudokuGridView gridView;
    private ButtonsGridView buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        viewModel = new SudokuPuzzleViewModel();
        viewModel = new SudokuPuzzleViewModel();
        gridView = findViewById(R.id.sudokuGridView);
        buttonView = findViewById(R.id.buttonsgridview);
        buttonView.initialize(new ChangeValueCommand(gridView));

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);

        Intent intent = getIntent();
        Integer level = intent.getIntExtra("level",1);
        gridView.initialize(level, viewModel);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.resetButton:
                gridView.reset();
                break;
            case R.id.checkButton:

                LayoutInflater inflater = getLayoutInflater();

                if (viewModel.isPuzzleSolved())
               {
                   layout = inflater.inflate(R.layout.ok_check_game_custom_toast,
                           (ViewGroup)findViewById(R.id.ok_custom_toast_container));
                   // TODO: ask if start new game
               }
               else
               {
                   layout = inflater.inflate(R.layout.not_ok_check_game_custom_toast,
                           (ViewGroup)findViewById(R.id.not_ok_custom_toast_container));
               }
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                break;
            default:
                break;
        }
    }
}
