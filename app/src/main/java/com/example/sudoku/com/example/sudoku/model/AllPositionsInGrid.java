package com.example.sudoku.com.example.sudoku.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class AllPositionsInGrid implements Iterable<Position> {
    @Override
    public Iterator<Position> iterator() {
        return new Iterator<Position>() {
            int currentRow = -1;
            int currentColumn = GridSettings.GRID_SIZE - 1;

            @Override
            public boolean hasNext() {
                return !isAtEndOfGrid();
            }

            @Override
            public Position next() {
                if (isAtEndOfGrid()) throw new NoSuchElementException();
                final boolean isOnLastColumn = isOnLastColumn();
                final int nextColumn = isOnLastColumn ? 0 : currentColumn + 1;
                final int nextRow = isOnLastColumn ? currentRow + 1 : currentRow;
                currentRow = nextRow;
                currentColumn = nextColumn;
                return new Position(nextRow, nextColumn);
            }

            private boolean isAtEndOfGrid() {
                return isOnLastRow() && isOnLastColumn();
            }

            private boolean isOnLastRow() {
                return currentRow == GridSettings.GRID_SIZE - 1;
            }

            private boolean isOnLastColumn() {
                return currentColumn == GridSettings.GRID_SIZE - 1;
            }
        };
    }
}
