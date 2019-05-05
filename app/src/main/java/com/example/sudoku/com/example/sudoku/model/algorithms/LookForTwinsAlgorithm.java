package com.example.sudoku.com.example.sudoku.model.algorithms;

import com.example.sudoku.com.example.sudoku.model.Cell;
import com.example.sudoku.com.example.sudoku.model.CellCollection;
import com.example.sudoku.com.example.sudoku.model.CellGroup;

public class LookForTwinsAlgorithm extends AlgorithmProcessor {
    private CheckType method;

    public LookForTwinsAlgorithm(CheckType method) {
        this.method = method;
    }

    private boolean lookForTwinsInGroups(CellGroup[] groups) throws Exception {
        boolean changes = false;

        for (CellGroup cellGroup : groups) {
            Cell[] cells = cellGroup.getCells();
            for (Cell cell : cells) {
                if (cell.getValue() == 0 && cell.getPossibleValuesCount() == 2) {
                    for (Cell cellTwo : cells) {
                        if (cell != cellTwo && cellTwo.checkIfPossibleValueListIsTheSame(cell.getPossibleValues())) {
                            for (Cell cellThird : cells) {
                                if (cellThird.getValue() == 0 && cellThird != cell && cellThird != cellTwo) {

                                    if (cellThird.getPossibleValues().removeAll(cell.getPossibleValues())) {
                                        changes = true;
                                    }
                                    if (cellThird.getPossibleValuesCount() == 0)
                                        throw new Exception("Invalid Move twins in rows");

                                    if (cellThird.updateValueIfReady()) {
                                        counter.addToScore(3);
                                    }
                                }

                            }
                        }

                    }
                }

            }
        }
        return changes;
    }

    @Override
    public boolean solvePuzzle(CellCollection group) throws Exception {
        if (method == CheckType.Column)
            return lookForTwinsInGroups(group.getColumns());
        else if (method == CheckType.Rows)
            return lookForTwinsInGroups(group.getRows());
        else
            return lookForTwinsInGroups(group.getSectors());
    }
}
