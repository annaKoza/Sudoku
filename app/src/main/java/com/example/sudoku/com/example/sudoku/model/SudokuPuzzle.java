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
                if (collection.getCellOnPosition(r, c).getValue() == 0)
                    emptyCells.addCoordinate(r, c);
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
                Cell cellToAdd = collection.getCellOnPosition(r, c);

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
                    emptyCells[empty - i] = collection.getCellOnPosition(8 - r, 8 - c);
                    collection.getCellOnPosition(8 - r, 8 - c).setValue(0);
                    collection.getCellOnPosition(8 - r, 8 - c).getPossibleValues().clear();
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
                cells_backup[r][c] = (Cell) collection.getCellOnPosition(r, c).clone();
                collection.getCellOnPosition(r, c).ClearHistory();
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
                if (emptyCellsCoordinates.contains(r, c)) {
                    collection.getCellOnPosition(r, c).setValue(0);
                    collection.getCellOnPosition(r, c).setEditable(true);
                } else
                    collection.getCellOnPosition(r, c).setEditable(false);
            }
        }
    }

    private void vacateAnotherPairOfCells(Cell[][] solvedCellsBackup) throws Exception {
        int c, r;

        removeRandomCoordinateFromEmptyCells();
        addRandomCoordinateToEmptyCells();

        for (int row = 0; row < GridSettings.GRID_SIZE; row++) {
            for (int col = 0; col < GridSettings.GRID_SIZE; col++) {
                if (!emptyCellsCoordinates.contains(row, col)) {
                    collection.getCellOnPosition(row, col).getPossibleValues().clear();
                    collection.getCellOnPosition(row, col).setValue(solvedCellsBackup[row][col].getValue());
                    collection.getCellOnPosition(row, col).getPossibleValues().add(solvedCellsBackup[row][col].getValue());
                } else {
                    collection.getCellOnPosition(row, col).getPossibleValues().clear();
                    collection.getCellOnPosition(row, col).setValue(0);
                }
            }
        }
    }

    private void addRandomCoordinateToEmptyCells() {
        int c;
        int r;
        do {
            c = randomNumber(0, 8);
            r = randomNumber(0, 8);
        }
        while (emptyCellsCoordinates.contains(r, c));

        emptyCellsCoordinates.addCoordinate(r, c);
        emptyCellsCoordinates.addCoordinate(8 - r, 8 - c);
    }

    private void removeRandomCoordinateFromEmptyCells() {
        int c;
        int r;
        do {
            c = randomNumber(0, 8);
            r = randomNumber(0, 8);
        }
        while (!emptyCellsCoordinates.contains(r, c));

        emptyCellsCoordinates.removeCoordinate(r, c);
        emptyCellsCoordinates.removeCoordinate(8 - r, 8 - c);
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
                Cell cell = collection.getCellOnPosition(r, c);
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
