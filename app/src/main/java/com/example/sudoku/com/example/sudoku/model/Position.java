package com.example.sudoku.com.example.sudoku.model;

import java.util.Objects;

public final class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        if (row < 0
                || row >= GridSettings.GRID_SIZE
                || column < 0
                || column >= GridSettings.GRID_SIZE)
            throw new IllegalArgumentException(
                    String.format("Row and column must be a value between 0 and %d", GridSettings.GRID_SIZE));
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return String.format("row: %d, column: %d", row, column);
    }
}
