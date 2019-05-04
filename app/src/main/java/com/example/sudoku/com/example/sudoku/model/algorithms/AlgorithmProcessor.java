package com.example.sudoku.com.example.sudoku.model.algorithms;

import com.example.sudoku.com.example.sudoku.model.CellCollection;
import com.example.sudoku.com.example.sudoku.model.ScoreCounter;

public abstract class AlgorithmProcessor {
    protected static ScoreCounter counter;
    private AlgorithmProcessor nextAlgorithm;

    public static void setScoreCounter(ScoreCounter scoreCounter) {
        counter = scoreCounter;
    }

    protected abstract boolean solvePuzzle(CellCollection group) throws Exception;

    public AlgorithmProcessor linkWith(AlgorithmProcessor next) {
        this.nextAlgorithm = next;
        return nextAlgorithm;
    }

    public boolean checkNext(CellCollection collection) throws Exception {
        Boolean continueSolving;
        do {
            if (nextAlgorithm != null) {
                boolean nextResult = nextAlgorithm.checkNext(collection);
                if (nextResult) {
                    return true;
                }
            }
            continueSolving = solvePuzzle(collection);
            if (collection.isPuzzleSolved()) {
                return true;
            }
        }
        while (continueSolving);
        return false;
    }
}

