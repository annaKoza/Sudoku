package com.example.sudoku.viewModel;
import com.example.sudoku.com.example.sudoku.model.Cell;
import com.example.sudoku.com.example.sudoku.model.GridSettings;
import com.example.sudoku.com.example.sudoku.model.SudokuPuzzle;

import java.util.ArrayList;
import java.util.List;

public class SudokuPuzzleViewModel {

    private int currentPositionR;
    private int currentPositionC;
    private SudokuPuzzle model;
    private Cell[][] cells;

    public SudokuPuzzleViewModel() {
        model = new SudokuPuzzle();
        cells = model.getCells();
    }

    public void setCurrentPosition(int r, int c) {
        currentPositionC = c;
        currentPositionR = r;
    }

    public Cell[][] solve() {
        return model.solve();
    }

    public boolean checkPuzzleCorrectness() {
        return model.checkPuzzleCorrectness();
    }

    public Cell[][] getPuzzle(int level) throws Exception {
        cells = model.getPuzzle(level);
        return cells;
    }

    public List<Cell> getInvalidCells() {
        List<Cell> invalidCells = new ArrayList<>();
        if (model.checkPuzzleCorrectness())
            return invalidCells;

        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                if (!cells[r][c].isValid())
                    invalidCells.add(cells[r][c]);
            }
        }
        return invalidCells;
    }
    public Cell[][] getPuzzle() {
        return cells;
    }

    public void setValueOnCurrentPosition(int number) {
        cells[currentPositionR][currentPositionC].setValue(number);
    }

    public int[] getCurrentPosition() {
        return new int[]{currentPositionR, currentPositionC};
    }

    public Cell[][] resetPuzzle() {
        return model.resetPuzzle();
    }

    public boolean checkPuzzleSolution() {
        return model.checkPuzzleSolution();
    }
}
