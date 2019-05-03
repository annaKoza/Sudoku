package com.example.sudoku.view.commands;

import com.example.sudoku.view.sudokuGrid.SudokuGridView;

public class ChangeValueCommand implements IValueCommand {
    private int newValue;
    private int oldValue;
    private SudokuGridView gridView;

    public ChangeValueCommand(SudokuGridView gridView) {
        this.gridView = gridView;
    }

    @Override
    public void execute(int newValue) {
        this.oldValue = this.newValue;
        this.newValue = newValue;
        gridView.setItem(newValue);
    }

}
