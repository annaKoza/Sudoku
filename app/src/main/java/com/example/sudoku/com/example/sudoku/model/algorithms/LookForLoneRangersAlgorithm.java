package com.example.sudoku.com.example.sudoku.model.algorithms;

import com.example.sudoku.com.example.sudoku.model.Cell;
import com.example.sudoku.com.example.sudoku.model.CellCollection;
import com.example.sudoku.com.example.sudoku.model.CellGroup;
import com.example.sudoku.com.example.sudoku.model.GridSettings;

public class LookForLoneRangersAlgorithm extends AlgorithmProcessor {
    private CheckType method;

    public LookForLoneRangersAlgorithm(CheckType method) {
        this.method = method;
    }

    private boolean lookForLoneRangersInGroups(CellGroup[] group) {
        boolean changes = false;
        int occurrence;
        Cell cellToChange = null;

        for (CellGroup cellGroup : group) {
            for (int n = 1; n <= GridSettings.GRID_SIZE; n++) {
                occurrence = 0;
                for (Cell cell : cellGroup.getCells()) {

                    if (cell.getValue() == 0 && cell.checkIfContainsPossibleValue(n)) {
                        occurrence += 1;

                        if (occurrence > 1)
                            break;

                        cellToChange = cell;
                    }
                }
                if (occurrence == 1) {
                    cellToChange.setValue(n);
                    changes = true;
                    counter.addToScore(2);
                    cellToChange = null;
                }
            }
        }
        return changes;
    }

    @Override
    public boolean solvePuzzle(CellCollection group) throws Exception {
        if (method == CheckType.Column)
            return lookForLoneRangersInGroups(group.getColumns());
        else if (method == CheckType.Rows)
            return lookForLoneRangersInGroups(group.getRows());
        else
            return lookForLoneRangersInGroups(group.getSectors());
    }
}
