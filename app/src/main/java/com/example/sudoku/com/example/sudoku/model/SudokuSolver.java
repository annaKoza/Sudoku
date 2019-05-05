package com.example.sudoku.com.example.sudoku.model;

import com.example.sudoku.com.example.sudoku.model.algorithms.AlgorithmProcessor;
import com.example.sudoku.com.example.sudoku.model.algorithms.CheckColumnsAndRowsAlgorithm;
import com.example.sudoku.com.example.sudoku.model.algorithms.CheckType;
import com.example.sudoku.com.example.sudoku.model.algorithms.LookForLoneRangersAlgorithm;
import com.example.sudoku.com.example.sudoku.model.algorithms.LookForTripletsAlgorithm;
import com.example.sudoku.com.example.sudoku.model.algorithms.LookForTwinsAlgorithm;

import java.util.List;

class SudokuSolver {
    private ScoreCounter scoreCounter = new ScoreCounter();
    private AlgorithmProcessor chainOfAlgorithms;
    private CellCollection collection;

    SudokuSolver(CellCollection collection) {
        this.collection = collection;
        chainOfAlgorithms = new LookForTripletsAlgorithm(CheckType.Column);
        chainOfAlgorithms.linkWith(new LookForTripletsAlgorithm(CheckType.Rows))
                .linkWith(new LookForTripletsAlgorithm(CheckType.Sectors))
                .linkWith(new LookForTwinsAlgorithm(CheckType.Column))
                .linkWith(new LookForTwinsAlgorithm(CheckType.Rows))
                .linkWith(new LookForTwinsAlgorithm(CheckType.Sectors))
                .linkWith(new LookForLoneRangersAlgorithm(CheckType.Column))
                .linkWith(new LookForLoneRangersAlgorithm(CheckType.Rows))
                .linkWith(new LookForLoneRangersAlgorithm(CheckType.Sectors))
                .linkWith(new CheckColumnsAndRowsAlgorithm(CheckType.Rows));
        AlgorithmProcessor.setScoreCounter(scoreCounter);
    }

    boolean solvePuzzleByAlgorithms() throws Exception {

        try {
            return chainOfAlgorithms.checkNext(collection);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }
    }

    boolean solvePuzzleByBruteForce() {
        scoreCounter.addToScore(5);
        Cell cell = collection.findCellWithFewestPossibleValues();
        List<Integer> possibleValues = cell.getRandomizedPossibleValues();

        SaveCellsState();

        for (int i = 0; i < possibleValues.size(); i++) {
            cell.setValue(possibleValues.get(i));
            try {
                if (solvePuzzleByAlgorithms()) {
                    return true;
                } else {
                    if (solvePuzzleByBruteForce())
                        return true;
                }
            } catch (Exception ex) {
                scoreCounter.addToScore(5);
                LoadCellsState();
            }
        }
        return false;
    }

    int getScore() {
        return scoreCounter.getTotalScore();
    }

    void resetScore() {
        scoreCounter.resetScore();
    }

    private void LoadCellsState() {
        for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
            for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
                final Position position = new Position(r, c);
                collection.getCellOnPosition(position).LoadState();
            }
        }
    }

    private void SaveCellsState() {
        for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
            for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
                final Position position = new Position(r, c);
                collection.getCellOnPosition(position).SaveState();
            }
        }
    }
}
