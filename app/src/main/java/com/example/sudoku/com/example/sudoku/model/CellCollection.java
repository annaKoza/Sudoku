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

        for (final Position position : new AllPositionsInGrid()) {
            final int r = position.getRow();
            final int c = position.getColumn();
            cells[r][c] = new Cell(
                    position,
                    rows[r], columns[c], sectors[((c / 3) * 3) + (r / 3)]);

            rows[r].addCell(cells[r][c]);
            columns[c].addCell(cells[r][c]);
            sectors[((c / 3) * 3) + (r / 3)].addCell(cells[r][c]);
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
        return cells[position.getRow()][position.getColumn()];
    }

    public void markAllCellsAsValid() {
        for (final Position position : new AllPositionsInGrid()) {
            cells[position.getRow()][position.getColumn()].setValid(true);
        }
    }

    Cell findCellWithFewestPossibleValues() {
        Cell cell = null;
        int min = 10;
        for (final Position position : new AllPositionsInGrid()) {
            final int r = position.getRow();
            final int c = position.getColumn();
            if (cells[r][c].getValue() == 0 && cells[r][c].getPossibleValuesCount() < min) {
                min = cells[r][c].getPossibleValuesCount();
                cell = cells[r][c];
            }
        }
        return cell;
    }

}
