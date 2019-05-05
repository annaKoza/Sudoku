package com.example.sudoku.com.example.sudoku.model.algorithms;

import com.example.sudoku.com.example.sudoku.model.CellCollection;
import com.example.sudoku.com.example.sudoku.model.CellGroup;
import com.example.sudoku.com.example.sudoku.model.ScoreCounter;

public abstract class AlgorithmProcessor {
    static ScoreCounter counter;
    private AlgorithmProcessor nextAlgorithm;

    private boolean isPuzzleSolved(boolean takeEmptyCellsIntoAccount, CellCollection collection) {
        boolean valid = true;

        collection.markAllCellsAsValid();

        for (CellGroup row : collection.getRows()) {
            if (!row.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        for (CellGroup column : collection.getColumns()) {
            if (!column.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        for (CellGroup sector : collection.getSectors()) {
            if (!sector.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        return valid;
    }

    public static void setScoreCounter(ScoreCounter scoreCounter) {
        counter = scoreCounter;
    }

    protected abstract boolean solvePuzzle(CellCollection group) throws Exception;

    public AlgorithmProcessor linkWith(AlgorithmProcessor next) {
        this.nextAlgorithm = next;
        return nextAlgorithm;
    }

    public boolean checkNext(CellCollection collection) throws Exception {
        boolean continueSolving;
        do {
            if (nextAlgorithm != null) {
                boolean nextResult = nextAlgorithm.checkNext(collection);
                if (nextResult) {
                    return true;
                }
            }
            continueSolving = solvePuzzle(collection);
            if (isPuzzleSolved(true, collection)) {
                return true;
            }
        }
        while (continueSolving);
        return false;
    }
}

