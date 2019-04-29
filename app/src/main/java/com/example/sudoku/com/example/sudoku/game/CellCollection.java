package com.example.sudoku.com.example.sudoku.game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CellCollection {

    // ==================================================
    // Solve puzzle by brute force
    // ==================================================
    boolean bruteForceStop = false;
    private Cell[][] cells;
    private CellGroup[] rows;
    private CellGroup[] columns;
    private CellGroup[] sectors;
    private int totalscore;
    private Cell[][] cells_backup;
    private Cell[][] empty_cells_backup;

    private CellCollection()
    {
        empty_cells_backup = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];
        cells_backup = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];
        cells = new Cell[GridSettings.GRID_SIZE][GridSettings.GRID_SIZE];
        rows = new CellGroup[GridSettings.GRID_SIZE];
        columns = new CellGroup[GridSettings.GRID_SIZE];
        sectors = new CellGroup[GridSettings.GRID_SIZE];
        for(int i=0;i< GridSettings.GRID_SIZE;i++)
        {
            rows[i] = new CellGroup();
            columns[i] = new CellGroup();
            sectors[i] = new CellGroup();
        }

        for (int r=0; r<GridSettings.GRID_SIZE;r++)
        {
            for(int c=0; c<GridSettings.GRID_SIZE;c++)
            {
                cells[r][c] = new Cell(
                        c,
                        r, rows[r], columns[c], sectors[((c / 3) * 3) + (r / 3)]);

                rows[r].addCell(cells[r][c]);
                columns[c].addCell(cells[r][c]);
                sectors[((c / 3) * 3) + (r / 3)].addCell(cells[r][c]);
            }
        }
    }

    public static CellCollection createEmptyGrid()
    {
        return new CellCollection();
    }

    // ==================================================
    // Calculates the possible values for all the cell
    // ==================================================
    private boolean checkColumnsAndRows() throws Exception {
        boolean changes = false;
        for (int row = 0;row < 9;row++)
        {
            for (int col = 0;col < 9;col++)
            {
                if (cells[row][col].getValue() == 0)
                {
                    try
                    {
                        cells[row][col].calculatePossibleValues();
                    }
                    catch (Exception ex)
                    {
                        throw new Exception(ex.getMessage());
                    }

                    if(cells[row][col].updateValueIfReady())
                    {
                        changes = true;
                        totalscore += 1;
                    }
                }
            }
        }
        return changes;
    }

    // =========================================================
    // Look for Lone Rangers in Groups
    // =========================================================
    private boolean lookForLoneRangersInGroups(CellGroup[] group) {
        boolean changes = false;
        int occurrence;
        Cell cellToChange = null;

        for (CellGroup row: group)
        {
            for (int n = 1;n <= 9;n++)
            {
                occurrence = 0;
                for (Cell cell: row.getCells()) {

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

    // ==================================================
    // Look for Twins in Groups
    // ==================================================
    private boolean lookForTwinsInGroups(CellGroup[] groups) throws Exception {
        boolean changes = false;

        for (CellGroup row: groups)
        {
            Cell[] cells = row.getCells();
            for (Cell cell: cells)
            {
                // ---for each row, check each column in the row---
                if (cell.getValue() == 0 && cell.getPossiblevaluesCount() == 2)
                {
                    for (Cell cellTwo: cells)
                    {
                        if (cell != cellTwo && cellTwo.checkIfPossibleValueListIsTheSame(cell.getPossibleValues()))
                        {
                            for (Cell cellThird: cells)
                            {
                                if (cellThird.getValue() == 0 && cellThird != cell && cellThird != cellTwo)
                                {

                                    if(cellThird.getPossibleValues().removeAll(cell.getPossibleValues()))
                                    {
                                        changes = true;
                                    }
                                    // ---if possible value reduces to
                                    // empty string, then the user has
                                    // placed a move that results in
                                    // the puzzle not solvable---
                                    if (cellThird.getPossiblevaluesCount() == 0)
                                        throw new Exception("Invalid Move twins in rows");

                                    // ---if left with 1 possible value
                                    // for the current cell, cell is
                                    // confirmed---
                                    if (cellThird.updateValueIfReady())
                                    {
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

    // ==================================================
    // Look for Triplets in Groups
    // ==================================================
    private boolean lookForTripletsInGroups(CellGroup[] groups) throws Exception {
        boolean changes = false;
        for (CellGroup column: groups)
        {
            Cell[] columnCells = column.getCells();
            for (Cell cell: columnCells)
            {
                if (cell.getValue() == 0 && cell.getPossiblevaluesCount() == 3)
                {
                    List<Cell> selectedCells = new ArrayList<>();
                    selectedCells.add(cell);

                    // ---scan by mini-grid---
                    for(Cell cellSecond: columnCells)
                    {
                        if (cell != cellSecond
                                && (cell.checkIfPossibleValueListIsTheSame(cellSecond.getPossibleValues())
                                || cellSecond.getPossiblevaluesCount() == 2
                                && cell.checkIfContainsPossibleValue(cellSecond.getPossibleValues())))

                            selectedCells.add(cellSecond);
                    }

                    // --found 3 cells as triplets; remove all from the other cells---
                    if (selectedCells.size() == 3)
                    {
                        for (Cell cellThird: columnCells)
                        {
                            if (cellThird.getValue() == 0
                                    && cellThird != selectedCells.get(0)
                                    && cellThird != selectedCells.get(1)
                                    && cellThird != selectedCells.get(2)
                            )
                            {
                                if(cellThird.getPossibleValues().removeAll(cell.getPossibleValues()))
                                {
                                    changes = true;
                                }
                                // ---if possible value reduces to
                                // empty string, then the user has
                                // placed a move that results in
                                // the puzzle not solvable---
                                if (cellThird.getPossiblevaluesCount() == 0)
                                    throw new Exception("Invalid Move triplates in columns");

                                // ---if left with 1 possible value
                                // for the current cell, cell is
                                // confirmed---
                                if (cellThird.updateValueIfReady())
                                {
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

    // ==================================================
    // Steps to solve the puzzle
    // ==================================================
    private boolean solvePuzzle() throws Exception {
        boolean changes;
        boolean ExitLoop = false;
        try
        {
            do
            {
                do
                {
                    do
                    {
                        do
                        {
                            do
                            {
                                do
                                {
                                    do
                                    {
                                        do
                                        {
                                            do
                                            {
                                                do
                                                {
                                                    // Minigrid Elimination---
                                                    changes = checkColumnsAndRows();

                                                    if (isPuzzleSolved())
                                                    {
                                                        ExitLoop = true;
                                                        break;
                                                    }
                                                }
                                                while (changes);
                                                if (ExitLoop)
                                                    break;

                                                // ---Look for Lone Rangers in Minigrids---
                                                changes = lookForLoneRangersInGroups(sectors);

                                                if (isPuzzleSolved())
                                                {
                                                    ExitLoop = true;
                                                    break;
                                                }
                                            }
                                            while (changes);
                                            if (ExitLoop)
                                                break;

                                            // ---Look for Lone Rangers in Rows---
                                            changes = lookForLoneRangersInGroups(rows);

                                            if (isPuzzleSolved())
                                            {
                                                ExitLoop = true;
                                                break;
                                            }
                                        }
                                        while (changes);
                                        if (ExitLoop)
                                            break;

                                        // ---Look for Lone Rangers in Columns---
                                        changes = lookForLoneRangersInGroups(columns);

                                        if (isPuzzleSolved())
                                        {
                                            ExitLoop = true;
                                            break;
                                        }
                                    }
                                    while (changes);
                                    if (ExitLoop)
                                        break;

                                    // ---Look for Twins in Minigrids---
                                    changes = lookForTwinsInGroups(sectors);

                                    if (isPuzzleSolved())
                                    {
                                        ExitLoop = true;
                                        break;
                                    }
                                }
                                while (changes);
                                if (ExitLoop)
                                    break;

                                // ---Look for Twins in Rows---
                                changes = lookForTwinsInGroups(rows);

                                if (isPuzzleSolved())
                                {
                                    ExitLoop = true;
                                    break;
                                }

                            }
                            while (changes);
                            if (ExitLoop)
                                break;

                            // ---Look for Twins in Columns---
                            changes = lookForTwinsInGroups(columns);

                            if (isPuzzleSolved())
                            {
                                ExitLoop = true;
                                break;
                            }

                        }
                        while (changes);
                        if (ExitLoop)
                            break;

                        // ---Look for Triplets in Minigrids---
                        changes = lookForTripletsInGroups(sectors);

                        if (isPuzzleSolved())
                        {
                            ExitLoop = true;
                            break;
                        }

                    }
                    while (changes);
                    if (ExitLoop)
                        break;

                    // ---Look for Triplets in Rows---
                    changes = lookForTripletsInGroups(rows);

                    if (isPuzzleSolved())
                    {
                        ExitLoop = true;
                        break;
                    }

                }
                while (changes);
                if (ExitLoop)
                    break;

                // ---Look for Triplets in Columns---
                changes = lookForTripletsInGroups(columns);

                if (isPuzzleSolved())
                {
                    ExitLoop = true;
                    break;
                }

            }
            while (changes);
        }
        catch (Exception ex)
        {
            throw new Exception(ex.getMessage(),ex);
        }

        if (isPuzzleSolved()) {

            return true;
        }
        else
            return false;
    }

    private void solvePuzzleByBruteForce() throws Exception {
        totalscore += 5;
        Cell cell = findCellWithFewestPossibleValues();

        List<Integer> possibleValues = randomizeThePossibleValues(cell.getPossibleValues());
        // ---save cells state---
        for (int c = 0;c < GridSettings.GRID_SIZE;c++)
        {
            for (int r = 0;r< GridSettings.GRID_SIZE;r++)
            {
                cells[r][c].SaveState();
            }
        }


        for (int i = 0;i < possibleValues.size();i++)
        {
            // ---select one value and try---
            cell.setValue(possibleValues.get(i));
            try
            {
                if (solvePuzzle())
                {
                    // ---if the puzzle is solved, the recursion can stop now---
                    bruteForceStop = true;
                    return ;
                }
                else
                {
                    // ---no problem with current selection, proceed with next cell---
                    solvePuzzleByBruteForce();
                    if (bruteForceStop)
                        return;
                }
            }
            catch (Exception ex)
            {
                totalscore += 5;
                for (int c = 0;c < GridSettings.GRID_SIZE;c++)
                {
                    for (int r = 0;r< GridSettings.GRID_SIZE;r++)
                    {
                        cells[r][c].LoadState();
                    }
                }
            }
        }
    }

    // =========================================================
    // Find the cell with the small number of possible values
    // =========================================================
    private Cell findCellWithFewestPossibleValues() {
        Cell cell = null;
        int min = 10;
        for (int r = 0;r < 9;r++)
        {
            for (int c = 0;c < 9;c++)
            {
                if (cells[r][c].getValue() == 0 && cells[r][c].getPossiblevaluesCount() < min)
                {
                    min = cells[r][c].getPossiblevaluesCount();
                    cell = cells[r][c];
                }

            }
        }
        return cell;
    }

    // =========================================================
    // Randomly swap the list of possible values
    // =========================================================
    private List<Integer> randomizeThePossibleValues(List<Integer> values) {

        List<Integer> randomValues = new ArrayList<>(values);
        Collections.shuffle(randomValues);
        return randomValues;
    }

    // ============================================================
    // Generate a random number between the specified range
    // ============================================================
    private int randomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    // ============================================================
    // Get Puzzle
    // ============================================================
    public Cell[][] getPuzzle(int level) throws Exception {
        totalscore = 0;
        Cell[][] result;
        boolean breakMe = false;
        do
        {
            result = generateNewPuzzle(level);

            if (result != null)
            {
                // ---check if puzzle matches the level of difficult---
                switch(level)
                {
                    case 1:
                    {
                        if (totalscore >= 42 & totalscore <= 46)
                            breakMe = true;

                        break;
                    }
                    case 2:
                    {
                        if (totalscore >= 49 & totalscore <= 53)
                            breakMe = true;

                        break;
                    }
                    case 3:
                    {
                        if (totalscore >= 56 & totalscore <= 60)
                            breakMe = true;

                        break;
                    }
                    case 4:
                    {
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

    // ============================================================
    // Generate a new Sudoku puzzle
    // ============================================================
    private Cell[][] generateNewPuzzle(int level) throws Exception {

        cells_backup = new Cell[9][9];

        for (Cell[] row: cells)
        {
            for(Cell element: row )
                element.ResetCell();
        }

        try
        {
            if (!solvePuzzle())
                solvePuzzleByBruteForce();
        }
        catch (Exception ex)
        {
            return null;
        }

        for (int r = 0;r < 9;r++)
        {
            for (int c = 0;c < 9;c++)
            {
                cells_backup[r][c] = (Cell)cells[r][c].clone();
                cells[r][c].ClearHistory();
            }
        }

        empty_cells_backup = initializeEmptyCells(cells, level);
        bruteForceStop = false;

        // ---verify the puzzle has only one solution---
        int tries = 0;
        do
        {
            totalscore = 0;
            try
            {
                if (!solvePuzzle())
                {
                    // ---if puzzle is not solved and this is a level 1 to 3 puzzle---
                    if (level < 4)
                    {
                        // ---choose another pair of cells to empty---
                        vacateAnotherPairOfCells(empty_cells_backup, cells_backup);
                        tries += 1;
                    }
                    else
                    {
                        // ---level 4 puzzles does not guarantee single solution and potentially need guessing---
                        solvePuzzleByBruteForce();
                        break;
                    }
                }
                else
                    break;

            }
            catch (Exception ex)
            {
                return null;
            }

            // ---if too many tries, exit the loop---
            if (tries > 50)
                return null;
        }
        while (true);

        return empty_cells_backup;
    }

    private Cell[][] initializeEmptyCells(Cell[][] cells, int level) throws CloneNotSupportedException {

        int numberofemptycells = 0;
        Cell[][] bakupCells = new Cell[9][9];
        // ---set the number of empty cells based on the level of difficulty---
        switch(level)
        {
            case 1:
            {
                numberofemptycells = randomNumber(40,45);
                break;
            }
            case 2:
            {
                numberofemptycells = randomNumber(46,49);
                break;
            }
            case 3:
            {
                numberofemptycells = randomNumber(50,53);
                break;
            }
            case 4:
            {
                numberofemptycells = randomNumber(54,58);
                break;
            }
        }

        createEmptyCells(numberofemptycells);
        for (int r = 0;r < 9;r++)
        {
            for (int c = 0;c < 9;c++)
            {
                bakupCells[r][c] = (Cell)cells[r][c].clone();
            }
        }
        return bakupCells;
    }

    // ============================================================
    // Create empty cells in the grid
    // ============================================================
    private void createEmptyCells(int empty) {
        int c , r ;
        // ----choose random locations for empty cells----
        Cell[] emptyCells = new Cell[empty+1];
        for (int i = 0; i <= empty / 2 +1; i++)
        {
            boolean duplicate ;
            do
            {
                duplicate = false;
                do
                {
                    c = randomNumber(0,8);
                    r = randomNumber(0,4);
                }
                // ---get a cell in the first half of the grid
                while (r == 4 && c > 4);
                Cell cellToAdd = cells[r][c];

                for (int j = 0; j < i; j++)
                {
                    // ---if cell is already selected to be empty
                    if (emptyCells[j] == cellToAdd)
                    {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate)
                {
                    // ---set the empty cell---
                    emptyCells[i] = cellToAdd;
                    cellToAdd.setValue(0);
                    cellToAdd.getPossibleValues().clear();

                    // ---reflect the top half of the grid and make it symmetrical---
                    emptyCells[empty - i] = cells[8 - r][8 - c];
                    cells[8 - r][8 - c].setValue(0);
                    cells[8 - r][8 - c].getPossibleValues().clear();
                }
            }
            while (duplicate);
        }
    }

    // ============================================================
    // Vacate another pair of cells
    // ============================================================
    private void vacateAnotherPairOfCells(Cell[][] emptyCellsBackup, Cell[][] solvedCellsBackup) throws Exception {
        int c, r;

        do {
            // ---look for a pair of cells to restore---
            c = randomNumber(0, 8);
            r = randomNumber(0, 8);
        }
        while (emptyCellsBackup[r][c].getValue() !=0);

        Cell cell = solvedCellsBackup[r][c];
        Cell mirrorCell = solvedCellsBackup[8 - r][8 - c];
        emptyCellsBackup[r][c].CopyCell(cell);
        emptyCellsBackup[8 - r][8 - c].CopyCell(mirrorCell);

        do {
            // ---look for another pair of cells to vacate---
            c = randomNumber(0, 8);
            r = randomNumber(0, 8);
        }
        while (emptyCellsBackup[r][c].getValue() == 0);
        // ---remove the cell from the str---
        emptyCellsBackup[r][c].setValue(0);
        emptyCellsBackup[8 - r][8 - c].setValue(0);

        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                if (emptyCellsBackup[row][col].getValue() != 0)
                {
                    cells[row][col].getPossibleValues().clear();
                    cells[row][col].setValue(emptyCellsBackup[row][col].getValue());
                    cells[row][col].getPossibleValues().add(cells[row][col].getValue());
                }
                else
                {
                    cells[row][col].getPossibleValues().clear();
                    cells[row][col].setValue(0);
                }
            }
        }
    }

    public boolean isPuzzleSolved()
    {
        boolean valid = true;

        markAllCellsAsValid();

        for (CellGroup row : rows) {
            if (!row.validate()) {
                valid = false;
            }
        }
        for (CellGroup column : columns) {
            if (!column.validate()) {
                valid = false;
            }
        }
        for (CellGroup sector : sectors) {
            if (!sector.validate()) {
                valid = false;
            }
        }
        return valid;
    }

    private void markAllCellsAsValid() {
        for(int c=0;c<GridSettings.GRID_SIZE;c++)
        {
            for (int r=0;r<GridSettings.GRID_SIZE;r++)
            {
                cells[r][c].setValid(true);
            }
        }
    }
}
