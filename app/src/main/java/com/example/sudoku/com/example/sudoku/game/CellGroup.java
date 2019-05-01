package com.example.sudoku.com.example.sudoku.game;

import java.util.HashMap;
import java.util.Map;

public class CellGroup {
    private Cell[] cells;
    private int count;
    public CellGroup()
    {
        cells = new Cell[GridSettings.GRID_SIZE];
        count = 0;
    }
    public void addCell(Cell cell)
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
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int value = cell.getValue();
            if (cellsByValue.get(value) != null)
            {
                cells[i].setValid(false);
                cellsByValue.get(value).setValid(false);
                if(!takeEmptyIntoAccount && value == 0)
                    continue;
                repetitionNumber++;
            }
            else {
                cellsByValue.put(value, cell);
            }

        }
        if(cellsByValue.containsKey(0) && takeEmptyIntoAccount) repetitionNumber++;
        return repetitionNumber == 0;
    }
}
