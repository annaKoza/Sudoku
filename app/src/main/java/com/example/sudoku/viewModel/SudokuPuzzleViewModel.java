package com.example.sudoku.viewModel;

import com.example.sudoku.com.example.sudoku.model.Cell;
import com.example.sudoku.com.example.sudoku.model.CellCollection;

public class SudokuPuzzleViewModel {

    private int currentPositionR;
    private int currentPositionC;
    private CellCollection model;
    private Cell[][] cells;

    public SudokuPuzzleViewModel() {
        model = CellCollection.createEmptyGrid();
    }

    public void setCurrentPosition(int r, int c) {
        currentPositionC = c;
        currentPositionR = r;
    }

    public Cell[][] solve() {
        return model.solve();
    }

    public boolean canPuzzleBeSolved() {
        return model.isPuzzleSolvable();
    }

    public Cell[][] getPuzzle(int level) throws Exception {
        cells = model.getPuzzle(level);
        return cells;
    }

    public Cell[][] getPuzzle() {
        return cells;
    }

    public void setValueOnCurrentPosition(int number) {
        model.getCellOnPosition(currentPositionR, currentPositionC).setValue(number);
    }

    public int[] getCurrentPosition() {
        return new int[]{currentPositionR, currentPositionC};
    }

    public Cell[][] resetPuzzle() {
        return model.resetPuzzle();
    }

    public boolean isPuzzleSolved() {
        return model.isPuzzleSolved();
    }
}
