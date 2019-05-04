package com.example.sudoku.com.example.sudoku.model.algorithms;

import com.example.sudoku.com.example.sudoku.model.Cell;
import com.example.sudoku.com.example.sudoku.model.CellCollection;
import com.example.sudoku.com.example.sudoku.model.CellGroup;

public final class CheckColumnsAndRowsAlgorithm extends AlgorithmProcessor {
    private CheckType method;

    public CheckColumnsAndRowsAlgorithm(CheckType method) {
        this.method = method;
    }

    private boolean checkColumnAndRowsInGroups(CellGroup[] group) throws Exception {
        boolean changes = false;
        for (CellGroup rows : group) {
            for (Cell element : rows.getCells()) {
                if (element.getValue() == 0) {
                    try {
                        element.calculatePossibleValues();
                    } catch (Exception ex) {
                        throw new Exception(ex.getMessage());
                    }

                    if (element.updateValueIfReady()) {
                        changes = true;
                        counter.addToScore(1);
                    }
                }
            }
        }

        return changes;
    }

    @Override
    public boolean solvePuzzle(CellCollection group) throws Exception {
        if (method == CheckType.Column)
            return checkColumnAndRowsInGroups(group.getColumns());
        else if (method == CheckType.Rows)
            return checkColumnAndRowsInGroups(group.getRows());
        else
            return checkColumnAndRowsInGroups(group.getSectors());
    }
}
