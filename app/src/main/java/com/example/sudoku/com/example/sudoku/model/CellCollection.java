package com.example.sudoku.com.example.sudoku.model;

public class CellCollection {

    private Cell[][] cells;
    private CellGroup[] rows;
    private CellGroup[] columns;
    private CellGroup[] sectors;
    private CellCollection() {
        InitializeCollection();
    }

    private void InitializeCollection() {
        cells = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];
        rows = new CellGroup[GridSettings.GRID_SIZE];
        columns = new CellGroup[GridSettings.GRID_SIZE];
        sectors = new CellGroup[GridSettings.GRID_SIZE];
        for (int i = 0; i < GridSettings.GRID_SIZE; i++) {
            rows[i] = new CellGroup();
            columns[i] = new CellGroup();
            sectors[i] = new CellGroup();
        }

        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                cells[r][c] = new Cell(
                        new Position(r, c),
                        rows[r], columns[c], sectors[((c / 3) * 3) + (r / 3)]);

                rows[r].addCell(cells[r][c]);
                columns[c].addCell(cells[r][c]);
                sectors[((c / 3) * 3) + (r / 3)].addCell(cells[r][c]);
            }
        }
    }

    static CellCollection createEmptyGrid() {
        return new CellCollection();
    }

    public CellGroup[] getColumns() {
        return columns;
    }

    public CellGroup[] getRows() {
        return rows;
    }

    public CellGroup[] getSectors() {
        return sectors;
    }

    Cell[][] getCells() {
        return cells;
    }

    Cell getCellOnPosition(Position position) {
        if (position.getRow() < GridSettings.GRID_SIZE && position.getColumn() < GridSettings.GRID_SIZE)
            return cells[position.getRow()][position.getColumn()];
        else
            throw new IllegalArgumentException("out of SudokuPuzzle grid bound");
    }

    public void markAllCellsAsValid() {
        for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
            for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
                cells[r][c].setValid(true);
            }
        }
    }

    Cell findCellWithFewestPossibleValues() {
        Cell cell = null;
        int min = 10;
        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                if (cells[r][c].getValue() == 0 && cells[r][c].getPossibleValuesCount() < min) {
                    min = cells[r][c].getPossibleValuesCount();
                    cell = cells[r][c];
                }
            }
        }
        return cell;
    }

}
