package com.example.sudoku.com.example.sudoku.model.algorithms;

import com.example.sudoku.com.example.sudoku.model.Cell;
import com.example.sudoku.com.example.sudoku.model.CellCollection;
import com.example.sudoku.com.example.sudoku.model.CellGroup;

import java.util.ArrayList;
import java.util.List;

public class LookForTripletsAlgorithm extends AlgorithmProcessor {
    private CheckType method;

    public LookForTripletsAlgorithm(CheckType method) {
        this.method = method;
    }

    private boolean lookForTripletsInGroups(CellGroup[] groups) throws Exception {
        boolean changes = false;
        for (CellGroup column : groups) {
            Cell[] columnCells = column.getCells();
            for (Cell cell : columnCells) {
                if (cell.getValue() == 0 && cell.getPossibleValuesCount() == 3) {
                    List<Cell> selectedCells = new ArrayList<>();
                    selectedCells.add(cell);

                    for (Cell cellSecond : columnCells) {
                        if (cell != cellSecond
                                && (cell.checkIfPossibleValueListIsTheSame(cellSecond.getPossibleValues())
                                || cellSecond.getPossibleValuesCount() == 2
                                && cell.checkIfContainsPossibleValue(cellSecond.getPossibleValues())))

                            selectedCells.add(cellSecond);
                    }

                    if (selectedCells.size() == 3) {
                        for (Cell cellThird : columnCells) {
                            if (cellThird.getValue() == 0
                                    && cellThird != selectedCells.get(0)
                                    && cellThird != selectedCells.get(1)
                                    && cellThird != selectedCells.get(2)
                            ) {
                                if (cellThird.getPossibleValues().removeAll(cell.getPossibleValues())) {
                                    changes = true;
                                }
                                if (cellThird.getPossibleValuesCount() == 0)
                                    throw new Exception("Invalid Move triplates in columns");

                                if (cellThird.updateValueIfReady()) {
                                    counter.addToScore(4);
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
            return lookForTripletsInGroups(group.getColumns());
        else if (method == CheckType.Rows)
            return lookForTripletsInGroups(group.getRows());
        else
            return lookForTripletsInGroups(group.getSectors());
    }
}
