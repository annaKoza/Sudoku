package com.example.sudoku.com.example.sudoku.game;

import com.example.sudoku.com.example.sudoku.game.Cell;
import com.example.sudoku.com.example.sudoku.game.GridSettings;

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

    public Cell getCellOnPosition(int c, int r)
    {
        for(Cell cell: cells)
        {
            int[] position = cell.getPosition();
            if(position[0] == c && position[1] == r)
                return cell;
        }
        return null;
    }

    public boolean validate()
    {
        boolean valid = true;
        Map<Integer, Cell> cellsByValue = new HashMap<>();
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            int value = cell.getValue();
            if(value == 0)
            {
                valid = false;
            }
            else if (cellsByValue.get(value) != null) {
                cells[i].setValid(false);
                cellsByValue.get(value).setValid(false);
                valid = false;
            }
            else {
                cellsByValue.put(value, cell);
            }
        }
        return valid;
    }

}
