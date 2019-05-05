package com.example.sudoku.com.example.sudoku.model;

import java.util.Random;

public class SudokuPuzzle {
    private static final int MAX_TRIES = 50;
    private CellCollection collection;
    private Cell[][] cells_backup;
    private CoordinatesList emptyCellsCoordinates;
    private SudokuSolver solver;

    public SudokuPuzzle() {
        collection = CellCollection.createEmptyGrid();
        solver = new SudokuSolver(collection);
        emptyCellsCoordinates = new CoordinatesList();
        cells_backup = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];
    }

    private CoordinatesList initializeEmptyCells(int level) {

        int numberOfEmptyCells = 0;
        CoordinatesList emptyCells = new CoordinatesList();
        switch (level) {
            case 1: {
                numberOfEmptyCells = randomNumber(40, 45);
                break;
            }
            case 2: {
                numberOfEmptyCells = randomNumber(46, 49);
                break;
            }
            case 3: {
                numberOfEmptyCells = randomNumber(50, 53);
                break;
            }
            case 4: {
                numberOfEmptyCells = randomNumber(54, 58);
                break;
            }
        }

        createEmptyCells(numberOfEmptyCells);
        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                final Position position = new Position(r, c);
                if (collection.getCellOnPosition(position).getValue() == 0) {
                    emptyCells.addCoordinate(position);
                }
            }
        }
        return emptyCells;
    }

    private void createEmptyCells(int empty) {
        int c, r;
        Cell[] emptyCells = new Cell[empty + 1];
        for (int i = 0; i <= empty / 2 + 1; i++) {
            boolean duplicate;
            do {
                duplicate = false;
                do {
                    c = randomNumber(0, 8);
                    r = randomNumber(0, 4);
                }

                // get a cell in the first half of the grid
                while (r == 4 && c > 4);
                final Position position = new Position(r, c);
                Cell cellToAdd = collection.getCellOnPosition(position);

                for (int j = 0; j < i; j++) {
                    if (emptyCells[j] == cellToAdd) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    emptyCells[i] = cellToAdd;
                    cellToAdd.setValue(0);
                    cellToAdd.getPossibleValues().clear();

                    // reflect the top half of the grid and make it symmetrical
                    final Position reflectedPosition = new Position(8 - position.getRow(), 8 - position.getColumn());
                    emptyCells[empty - i] = collection.getCellOnPosition(reflectedPosition);
                    collection.getCellOnPosition(reflectedPosition).setValue(0);
                    collection.getCellOnPosition(reflectedPosition).getPossibleValues().clear();
                }
            }
            while (duplicate);
        }
    }

    private int randomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private boolean tryGenerateNewPuzzle(int level) throws Exception {

        cells_backup = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];

        for (CellGroup row : collection.getRows()) {
            for (Cell cell : row.getCells())
                cell.ResetCell();
        }

        try {
            if (!solver.solvePuzzleByAlgorithms())
                solver.solvePuzzleByBruteForce();
        } catch (Exception ex) {
            return false;
        }

        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                final Position position = new Position(r, c);
                cells_backup[r][c] = (Cell) collection.getCellOnPosition(position).clone();
                collection.getCellOnPosition(position).ClearHistory();
            }
        }

        emptyCellsCoordinates = initializeEmptyCells(level);

        boolean hasSolution = verifyIsOnlyOneSolution(level);
        if (hasSolution) {
            reloadCells();
            return true;
        }
        return false;
    }

    private boolean verifyIsOnlyOneSolution(int level) {
        int tries = 0;
        do {
            solver.resetScore();
            try {
                if (!solver.solvePuzzleByAlgorithms()) {
                    if (level < 3) {
                        vacateAnotherPairOfCells(cells_backup);
                        tries += 1;
                    } else {
                        //level 3,4 puzzles does not guarantee single solution and potentially need guessing
                        solver.solvePuzzleByBruteForce();
                        break;
                    }
                } else
                    break;

            } catch (Exception ex) {
                return false;
            }

            if (tries > MAX_TRIES)
                return false;
        }
        while (true);
        return true;
    }

    private void reloadCells() {
        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                final Position position = new Position(r, c);
                if (emptyCellsCoordinates.contains(position)) {
                    collection.getCellOnPosition(position).setValue(0);
                    collection.getCellOnPosition(position).setEditable(true);
                } else
                    collection.getCellOnPosition(position).setEditable(false);
            }
        }
    }

    private void vacateAnotherPairOfCells(Cell[][] solvedCellsBackup) throws Exception {
        int c, r;

        removeRandomCoordinateFromEmptyCells();
        addRandomCoordinateToEmptyCells();

        for (int row = 0; row < GridSettings.GRID_SIZE; row++) {
            for (int col = 0; col < GridSettings.GRID_SIZE; col++) {
                final Position position = new Position(row, col);
                if (!emptyCellsCoordinates.contains(position)) {
                    collection.getCellOnPosition(position).getPossibleValues().clear();
                    collection.getCellOnPosition(position).setValue(solvedCellsBackup[row][col].getValue());
                    collection.getCellOnPosition(position).getPossibleValues().add(solvedCellsBackup[row][col].getValue());
                } else {
                    collection.getCellOnPosition(position).getPossibleValues().clear();
                    collection.getCellOnPosition(position).setValue(0);
                }
            }
        }
    }

    private void addRandomCoordinateToEmptyCells() {
        Position position;
        do {
            final int c = randomNumber(0, 8);
            final int r = randomNumber(0, 8);
            position = new Position(r, c);
        }
        while (emptyCellsCoordinates.contains(position));

        emptyCellsCoordinates.addCoordinate(position);
        emptyCellsCoordinates.addCoordinate(new Position(8 - position.getRow(), 8 - position.getColumn()));
    }

    private void removeRandomCoordinateFromEmptyCells() {
        Position position;
        do {
            final int c = randomNumber(0, 8);
            final int r = randomNumber(0, 8);
            position = new Position(r, c);
        }
        while (!emptyCellsCoordinates.contains(position));

        emptyCellsCoordinates.removeCoordinate(position);
        emptyCellsCoordinates.removeCoordinate(new Position(8 - position.getRow(), 8 - position.getColumn()));
    }

    private boolean checkPuzzleSolution(boolean takeEmptyCellsIntoAccount) {
        boolean valid = true;

        collection.markAllCellsAsValid();

        for (CellGroup row : collection.getRows()) {
            if (!row.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        for (CellGroup column : collection.getColumns()) {
            if (!column.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        for (CellGroup sector : collection.getSectors()) {
            if (!sector.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        return valid;
    }

    public Cell[][] getPuzzle(int level) throws Exception {
        solver.resetScore();

        boolean breakMe = false;
        do {
            boolean result = tryGenerateNewPuzzle(level);
            if (result) {
                int totalScore = solver.getScore();
                switch (level) {
                    case 1: {
                        if (totalScore >= 42 & totalScore <= 48)
                            breakMe = true;

                        break;
                    }
                    case 2: {
                        if (totalScore >= 49 & totalScore <= 55)
                            breakMe = true;

                        break;
                    }
                    case 3: {
                        if (totalScore >= 56 & totalScore <= 111)
                            breakMe = true;

                        break;
                    }
                    case 4: {
                        if (totalScore >= 112 & totalScore <= 116)
                            breakMe = true;
                        break;
                    }
                }
            }
        }
        while (!breakMe);
        return collection.getCells();
    }

    public Cell[][] resetPuzzle() {
        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                final Position position = new Position(r, c);
                Cell cell = collection.getCellOnPosition(position);
                cell = cells_backup[r][c];
                reloadCells();
            }
        }
        return collection.getCells();
    }

    public Cell[][] solve() {
        try {
            if (!solver.solvePuzzleByAlgorithms()) {
                solver.solvePuzzleByBruteForce();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot solve puzzle");
        }
        return collection.getCells();
    }

    public boolean checkPuzzleSolution() {
        return checkPuzzleSolution(true);
    }

    public boolean checkPuzzleCorrectness() {
        return checkPuzzleSolution(false);
    }

    public Cell[][] getCells() {
        return collection.getCells();
    }
}
