package com.example.sudoku.com.example.sudoku.model;

import java.util.HashMap;
import java.util.Map;

public class CellGroup {
    private Cell[] cells;
    private int count;

    CellGroup()
    {
        cells = new Cell[GridSettings.GRID_SIZE];
        count = 0;
    }

    void addCell(Cell cell)
    {
        cells[count] = cell;
        count++;
    }
    public  Cell[] getCells()
    {
        return cells;
    }

    public boolean validate(boolean takeEmptyIntoAccount)
    {
        int repetitionNumber = 0;
        Map<Integer, Cell> cellsByValue = new HashMap<>();
        for (Cell cell : cells) {
            int value = cell.getValue();
            if (cellsByValue.get(value) != null) {
                cell.setValid(false);
                cellsByValue.get(value).setValid(false);
                if (!takeEmptyIntoAccount && value == 0) {
                    cell.setValid(true);
                    cellsByValue.get(value).setValid(true);
                    continue;
                }
                repetitionNumber++;
            } else {
                cellsByValue.put(value, cell);
            }

        }
        if(cellsByValue.containsKey(0) && takeEmptyIntoAccount) repetitionNumber++;
        return repetitionNumber == 0;
    }
}
