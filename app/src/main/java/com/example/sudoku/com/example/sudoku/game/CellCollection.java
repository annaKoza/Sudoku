package com.example.sudoku.com.example.sudoku.game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

class CoordinatesList {
    private HashMap<String, int[]> coordinates;

    public CoordinatesList() {
        coordinates = new HashMap<>();
    }

    public void addCoordinate(int r, int c) {
        String value = String.format("%d%d", r, c);
        coordinates.putIfAbsent(value, new int[]{r, c});
    }

    public boolean removeCoordinate(int r, int c) {
        String value = String.format("%d%d", r, c);
        if (!coordinates.containsKey(value))
            return false;
        coordinates.remove(value);
        return true;
    }

    public boolean contains(int r, int c) {
        return coordinates.containsKey(String.format("%d%d", r, c));
    }
}

public class CellCollection {

    boolean bruteForceStop = false;
    private Cell[][] cells;
    private CellGroup[] rows;
    private CellGroup[] columns;
    private CellGroup[] sectors;
    private int totalscore;
    private Cell[][] cells_backup;
    private CoordinatesList emptyCellsCoordinates;

    private CellCollection() {
        InitializeCollection();
    }

    public static CellCollection createEmptyGrid() {
        return new CellCollection();
    }

    private void InitializeCollection() {
        emptyCellsCoordinates = new CoordinatesList();
        cells_backup = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];
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
                        c,
                        r, rows[r], columns[c], sectors[((c / 3) * 3) + (r / 3)]);

                rows[r].addCell(cells[r][c]);
                columns[c].addCell(cells[r][c]);
                sectors[((c / 3) * 3) + (r / 3)].addCell(cells[r][c]);
            }
        }
    }

    private boolean checkColumnsAndRows() throws Exception {
        boolean changes = false;
        for (int row = 0; row < GridSettings.GRID_SIZE; row++) {
            for (int col = 0; col < GridSettings.GRID_SIZE; col++) {
                if (cells[row][col].getValue() == 0) {
                    try {
                        cells[row][col].calculatePossibleValues();
                    } catch (Exception ex) {
                        throw new Exception(ex.getMessage());
                    }

                    if (cells[row][col].updateValueIfReady()) {
                        changes = true;
                        totalscore += 1;
                    }
                }
            }
        }
        return changes;
    }

    private boolean lookForLoneRangersInGroups(CellGroup[] group) {
        boolean changes = false;
        int occurrence;
        Cell cellToChange = null;

        for (CellGroup row : group) {
            for (int n = 1; n <= GridSettings.GRID_SIZE; n++) {
                occurrence = 0;
                for (Cell cell : row.getCells()) {

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
                    totalscore += 2;
                    cellToChange = null;
                }
            }
        }
        return changes;
    }

    private boolean lookForTwinsInGroups(CellGroup[] groups) throws Exception {
        boolean changes = false;

        for (CellGroup row : groups) {
            Cell[] cells = row.getCells();
            for (Cell cell : cells) {
                if (cell.getValue() == 0 && cell.getPossiblevaluesCount() == 2) {
                    for (Cell cellTwo : cells) {
                        if (cell != cellTwo && cellTwo.checkIfPossibleValueListIsTheSame(cell.getPossibleValues())) {
                            for (Cell cellThird : cells) {
                                if (cellThird.getValue() == 0 && cellThird != cell && cellThird != cellTwo) {

                                    if (cellThird.getPossibleValues().removeAll(cell.getPossibleValues())) {
                                        changes = true;
                                    }
                                    if (cellThird.getPossiblevaluesCount() == 0)
                                        throw new Exception("Invalid Move twins in rows");

                                    if (cellThird.updateValueIfReady()) {
                                        totalscore += 3;
                                    }
                                }

                            }
                        }

                    }
                }

            }
        }
        return changes;
    }

    private boolean lookForTripletsInGroups(CellGroup[] groups) throws Exception {
        boolean changes = false;
        for (CellGroup column : groups) {
            Cell[] columnCells = column.getCells();
            for (Cell cell : columnCells) {
                if (cell.getValue() == 0 && cell.getPossiblevaluesCount() == 3) {
                    List<Cell> selectedCells = new ArrayList<>();
                    selectedCells.add(cell);

                    for (Cell cellSecond : columnCells) {
                        if (cell != cellSecond
                                && (cell.checkIfPossibleValueListIsTheSame(cellSecond.getPossibleValues())
                                || cellSecond.getPossiblevaluesCount() == 2
                                && cell.checkIfContainsPossibleValue(cellSecond.getPossibleValues())))

                            selectedCells.add(cellSecond);
                    }

                    if (selectedCells.size() == 3) {
                        for (Cell cellThird : columnCells) {
                            if (cellThird.getValue() == 0
                                    && cellThird != selectedCells.get(0)
                                    && cellThird != selectedCells.get(1)
                                    && cellThird != selectedCells.get(2)
                            ) {
                                if (cellThird.getPossibleValues().removeAll(cell.getPossibleValues())) {
                                    changes = true;
                                }
                                if (cellThird.getPossiblevaluesCount() == 0)
                                    throw new Exception("Invalid Move triplates in columns");

                                if (cellThird.updateValueIfReady()) {
                                    totalscore += 4;
                                }
                            }
                        }
                    }

                }

            }
        }
        return changes;
    }

    private boolean solvePuzzle() throws Exception {
        boolean changes;
        boolean ExitLoop = false;
        try {
            do {
                do {
                    do {
                        do {
                            do {
                                do {
                                    do {
                                        do {
                                            do {
                                                do {
                                                    // Minigrid Elimination---
                                                    changes = checkColumnsAndRows();

                                                    if (isPuzzleSolved()) {
                                                        ExitLoop = true;
                                                        break;
                                                    }
                                                }
                                                while (changes);
                                                if (ExitLoop)
                                                    break;

                                                // ---Look for Lone Rangers in Minigrids---
                                                changes = lookForLoneRangersInGroups(sectors);

                                                if (isPuzzleSolved()) {
                                                    ExitLoop = true;
                                                    break;
                                                }
                                            }
                                            while (changes);
                                            if (ExitLoop)
                                                break;

                                            // ---Look for Lone Rangers in Rows---
                                            changes = lookForLoneRangersInGroups(rows);

                                            if (isPuzzleSolved()) {
                                                ExitLoop = true;
                                                break;
                                            }
                                        }
                                        while (changes);
                                        if (ExitLoop)
                                            break;

                                        // ---Look for Lone Rangers in Columns---
                                        changes = lookForLoneRangersInGroups(columns);

                                        if (isPuzzleSolved()) {
                                            ExitLoop = true;
                                            break;
                                        }
                                    }
                                    while (changes);
                                    if (ExitLoop)
                                        break;

                                    // ---Look for Twins in Minigrids---
                                    changes = lookForTwinsInGroups(sectors);

                                    if (isPuzzleSolved()) {
                                        ExitLoop = true;
                                        break;
                                    }
                                }
                                while (changes);
                                if (ExitLoop)
                                    break;

                                // ---Look for Twins in Rows---
                                changes = lookForTwinsInGroups(rows);

                                if (isPuzzleSolved()) {
                                    ExitLoop = true;
                                    break;
                                }

                            }
                            while (changes);
                            if (ExitLoop)
                                break;

                            // ---Look for Twins in Columns---
                            changes = lookForTwinsInGroups(columns);

                            if (isPuzzleSolved()) {
                                ExitLoop = true;
                                break;
                            }

                        }
                        while (changes);
                        if (ExitLoop)
                            break;

                        // ---Look for Triplets in Minigrids---
                        changes = lookForTripletsInGroups(sectors);

                        if (isPuzzleSolved()) {
                            ExitLoop = true;
                            break;
                        }

                    }
                    while (changes);
                    if (ExitLoop)
                        break;

                    // ---Look for Triplets in Rows---
                    changes = lookForTripletsInGroups(rows);

                    if (isPuzzleSolved()) {
                        ExitLoop = true;
                        break;
                    }

                }
                while (changes);
                if (ExitLoop)
                    break;

                // ---Look for Triplets in Columns---
                changes = lookForTripletsInGroups(columns);

                if (isPuzzleSolved()) {
                    break;
                }

            }
            while (changes);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage(), ex);
        }

        if (isPuzzleSolved()) {

            return true;
        } else
            return false;
    }

    private void solvePuzzleByBruteForce() throws Exception {
        totalscore += 5;
        Cell cell = findCellWithFewestPossibleValues();

        List<Integer> possibleValues = cell.getRandomizedPossibleValues();
        // ---save cells state---
        for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
            for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
                cells[r][c].SaveState();
            }
        }

        for (int i = 0; i < possibleValues.size(); i++) {
            cell.setValue(possibleValues.get(i));
            try {
                if (solvePuzzle()) {
                    bruteForceStop = true;
                    return;
                } else {
                    solvePuzzleByBruteForce();
                    if (bruteForceStop)
                        return;
                }
            } catch (Exception ex) {
                totalscore += 5;
                for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                    for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
                        cells[r][c].LoadState();
                    }
                }
            }
        }
    }


    private Cell findCellWithFewestPossibleValues() {
        Cell cell = null;
        int min = 10;
        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                if (cells[r][c].getValue() == 0 && cells[r][c].getPossiblevaluesCount() < min) {
                    min = cells[r][c].getPossiblevaluesCount();
                    cell = cells[r][c];
                }
            }
        }
        return cell;
    }


    private int randomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public Cell[][] getPuzzle(int level) throws Exception {
        totalscore = 0;
        Cell[][] result;
        boolean breakMe = false;
        do {
            result = generateNewPuzzle(level);
            if (result != null) {
                switch (level) {
                    case 1: {
                        if (totalscore >= 42 & totalscore <= 48)
                            breakMe = true;

                        break;
                    }
                    case 2: {
                        if (totalscore >= 49 & totalscore <= 55)
                            breakMe = true;

                        break;
                    }
                    case 3: {
                        if (totalscore >= 56 & totalscore <= 111)
                            breakMe = true;

                        break;
                    }
                    case 4: {
                        if (totalscore >= 112 & totalscore <= 116)
                            breakMe = true;
                        break;
                    }

                }
            }
        }
        while (!breakMe);
        return result;
    }

    private Cell[][] generateNewPuzzle(int level) throws Exception {

        cells_backup = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];

        for (Cell[] row : cells) {
            for (Cell element : row)
                element.ResetCell();
        }

        try {
            if (!solvePuzzle())
                solvePuzzleByBruteForce();
        } catch (Exception ex) {
            return null;
        }

        for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                cells_backup[r][c] = (Cell) cells[r][c].clone();
                cells[r][c].ClearHistory();
            }
        }

        emptyCellsCoordinates = initializeEmptyCells(cells, level);
        bruteForceStop = false;

        boolean hasSolution = verifyIsOnlyOneSolution(level);
        if(hasSolution)
        {
            reloadCells();
            return cells;
        }
        return null;
    }

    private boolean verifyIsOnlyOneSolution(int level)
    {
        int tries = 0;
        do {
            totalscore = 0;
            try {
                if (!solvePuzzle()) {
                    if (level < 3) {
                        vacateAnotherPairOfCells(cells_backup);
                        tries += 1;
                    } else {
                        //level 3,4 puzzles does not guarantee single solution and potentially need guessing
                        solvePuzzleByBruteForce();
                        break;
                    }
                } else
                    break;

            } catch (Exception ex) {
                return false;
            }

            if (tries > 50)
                return false;
        }
        while (true);

        return true;
    }
    private void reloadCells()
    {
        for (int r =0;r<GridSettings.GRID_SIZE;r++)
        {
            for(int c=0;c<GridSettings.GRID_SIZE;c++) {
                if(emptyCellsCoordinates.contains(r,c)) {
                    cells[r][c].setValue(0);
                    cells[r][c].setEditable(true);
                }
                else
                    cells[r][c].setEditable(false);
            }
        }
    }

    private CoordinatesList initializeEmptyCells(Cell[][] cells, int level) throws CloneNotSupportedException {

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
                if (cells[r][c].getValue() == 0)
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
                Cell cellToAdd = cells[r][c];

                for (int j = 0; j < i; j++) {
                    if (emptyCells[j] == cellToAdd) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate)
                {
                    emptyCells[i] = cellToAdd;
                    cellToAdd.setValue(0);
                    cellToAdd.getPossibleValues().clear();

                    // reflect the top half of the grid and make it symmetrical
                    emptyCells[empty - i] = cells[8 - r][8 - c];
                    cells[8 - r][8 - c].setValue(0);
                    cells[8 - r][8 - c].getPossibleValues().clear();
                }
            }
            while (duplicate);
        }
    }

    private void vacateAnotherPairOfCells(Cell[][] solvedCellsBackup) throws Exception {
        int c, r;

        do {
            c = randomNumber(0, 8);
            r = randomNumber(0, 8);
        }
        while (!emptyCellsCoordinates.contains(r, c));

        emptyCellsCoordinates.removeCoordinate(r, c);
        emptyCellsCoordinates.removeCoordinate(8 - r, 8 - c);

        do {
            c = randomNumber(0, 8);
            r = randomNumber(0, 8);
        }
        while (emptyCellsCoordinates.contains(r, c));

        emptyCellsCoordinates.addCoordinate(r, c);
        emptyCellsCoordinates.addCoordinate(8 - r, 8 - c);

        for (int row = 0; row < GridSettings.GRID_SIZE; row++) {
            for (int col = 0; col < GridSettings.GRID_SIZE; col++) {
                if (!emptyCellsCoordinates.contains(row, col)) {
                    cells[row][col].getPossibleValues().clear();
                    cells[row][col].setValue(solvedCellsBackup[row][col].getValue());
                    cells[row][col].getPossibleValues().add(solvedCellsBackup[row][col].getValue());
                } else {
                    cells[row][col].getPossibleValues().clear();
                    cells[row][col].setValue(0);
                }
            }
        }
    }

    public boolean isPuzzleSolved()
    {
       return isPuzzleSolved(true);
    }
    public boolean isPuzzleSolvable()
    {
     return isPuzzleSolved(false);
    }
    private boolean isPuzzleSolved(boolean takeEmptyCellsIntoAccount) {
        boolean valid = true;

        markAllCellsAsValid();

        for (CellGroup row : rows) {
            if (!row.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        for (CellGroup column : columns) {
            if (!column.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        for (CellGroup sector : sectors) {
            if (!sector.validate(takeEmptyCellsIntoAccount)) {
                valid = false;
            }
        }
        return valid;
    }

    private void markAllCellsAsValid() {
        for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
            for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
                cells[r][c].setValid(true);
            }
        }
    }
    public Cell[][] getCells()
    {
        return cells;
    }
    public Cell getCellOnPosition(int r, int c)
    {
        if(r<GridSettings.GRID_SIZE && c <GridSettings.GRID_SIZE)
            return cells[r][c];
        else
            throw new IllegalArgumentException("out of Sudoku grid bound");
    }

    public boolean solveEmpty() {
        try {
            if (solvePuzzle()) {
                bruteForceStop = true;
                return true;
            } else {
                solvePuzzleByBruteForce();
                if (bruteForceStop)
                    return true;
            }
        } catch (Exception ex) {
           return false;
        }
        return false;
    }

    public Cell[][] resetPuzzle() {
        for (int r =0;r<GridSettings.GRID_SIZE;r++) {
            for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
                cells[r][c] = cells_backup[r][c];
                reloadCells();
            }
        }
        return  cells;
    }
}
