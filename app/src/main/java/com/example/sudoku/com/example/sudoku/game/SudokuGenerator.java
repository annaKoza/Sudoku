package com.example.sudoku.com.example.sudoku.game;

import java.util.Random;
import java.util.Stack;

public class SudokuGenerator
{
    // ---used to represent the values in the grid---
    private int[][] actual = new int[10][10];
    // ---used to represent the possible values of cells in the grid---
    private String[][] possible = new String[10][10];
    // ---indicate if the brute-force subroutine should stop---
    private boolean BruteForceStop = false;
    // ---used to store the state of the grid---
    private Stack<int[][]> ActualStack = new Stack<>();
    private Stack<String[][]> PossibleStack = new Stack<>();
    // ---store the total score accumulated---
    private int totalscore;
    // ---backup a copy of the Actual array---
    private int[][] actual_backup = new int[10][10];
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
                                                    // Minigrids---
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

                                                // ---Look for Lone Rangers in
                                                // Minigrids---
                                                changes = lookForLoneRangersinMinigrids();
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
                                            changes = lookForLoneRangersinRows();
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
                                        changes = lookForLoneRangersinColumns();
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
                                    changes = lookForTwinsinMinigrids();
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
                                changes = lookForTwinsinRows();
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
                            changes = lookForTwinsinColumns();
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
                        changes = lookForTripletsinMinigrids();
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
                    changes = lookForTripletsinRows();
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
                changes = lookForTripletsinColumns();
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
            throw new Exception("Invalid Move");
        }

        if (isPuzzleSolved())
            return true;
        else
            return false;
    }

    // ==================================================
    // Calculates the possible values for all the cell
    // ==================================================
    private boolean checkColumnsAndRows() throws Exception {
        boolean changes = false;
        for (int row = 1;row <= 9;row++)
        {
            for (int col = 1;col <= 9;col++)
            {
                // ---check all cells---
                if (actual[col][row] == 0)
                {
                    try
                    {
                        possible[col][row] = calculatePossibleValues(col,row);
                    }
                    catch (Exception ex)
                    {
                        throw new Exception("Invalid Move");
                    }

                    if (possible[col][row].length() == 1)
                    {
                        // ---number is confirmed---
                        actual[col][row] = Integer.valueOf(possible[col][row]);
                        changes = true;
                        // ---accumulate the total score---
                        totalscore += 1;
                    }

                }

            }
        }
        return changes;
    }

    // ==================================================
    // Calculates the possible values for a cell
    // ==================================================
    private String calculatePossibleValues(int col,int row) throws Exception {
        String str;
        if (possible[col][row] == "")
        str = "123456789";
        else
        str = possible[col][row];
        int r,c;

        //check col
        for (r = 1;r <= 9;r++)
        {
            // ---Step (1) check by column---
            if (actual[col][r] != 0)
            // ---that means there is a actual value in it---
            str = str.replace(String.valueOf(actual[col][r]),"");

        }
        //check row
        for (c = 1;c <= 9;c++)
        {
            // ---Step (2) check by row---
            if (actual[c][row] != 0)
            // ---that means there is a actual value in it---
            str = str.replace(String.valueOf(actual[c][row]),"");

        }
        // check minigrid
        int startC,startR;
        startC = col - (col - 1) % 3;
        startR = row - (row - 1) % 3;
        for (int rr = startR;rr <= startR + 2;rr++)
        {
            for (int cc = startC;cc <= startC + 2;cc++)
            {
                if (actual[cc][rr] != 0)
                str = str.replace(String.valueOf(actual[cc][rr]),"");

            }
        }
        // ---if possible value is ""then error---
        if (str =="")
            throw new Exception("Invalid Move");

        return str;
    }

    // ==================================================
    // Look for lone rangers in Minigrids
    // ==================================================
    private boolean lookForLoneRangersinMinigrids() throws Exception {
        boolean changes = false;
        boolean NextMiniGrid;
        int occurrence ;
        int cPos = 0,rPos = 0;
        for (int n = 1;n <= 9;n++)
        {
            for (int r = 1;r <= 9;r += 3)
            {
                for (int c = 1;c <= 9;c += 3)
                {
                    // ---check for each number from 1 to 9---
                    // ---check the 9 mini-grids---
                    NextMiniGrid = false;
                    // ---check within the mini-grid---
                    occurrence = 0;
                    for (int rr = 0;rr <= 2;rr++)
                    {
                        for (int cc = 0;cc <= 2;cc++)
                        {
                            if (actual[c + cc][r + rr] == 0 && possible[c + cc][r + rr].contains(String.valueOf(n)))
                            {
                                occurrence += 1;
                                cPos = c + cc;
                                rPos = r + rr;
                                if (occurrence > 1)
                                {
                                    NextMiniGrid = true;
                                    break;
                                }

                            }

                        }
                        if (NextMiniGrid)
                            break;

                    }
                    if (!NextMiniGrid && occurrence == 1)
                    {
                        // ---that means number is confirmed---
                        actual[cPos][rPos] = n;
                        changes = true;
                        // ---accumulate the total score---
                        totalscore += 2;
                    }

                }
            }
        }
        return changes;
    }

    // =========================================================
    // Look for Lone Rangers in Rows
    // =========================================================
    private boolean lookForLoneRangersinRows() throws Exception {
        boolean changes = false;
        int occurrence ;
        int cPos = 0, rPos = 0;
        for (int r = 1;r <= 9;r++)
        {
            for (int n = 1;n <= 9;n++)
            {
                // ---check by row----
                occurrence = 0;
                for (int c = 1;c <= 9;c++)
                {
                    if (actual[c][r] == 0 && possible[c][r].contains(String.valueOf(n)))
                    {
                        occurrence += 1;
                        // ---if multiple occurrence, not a lone ranger anymore
                        if (occurrence > 1)
                            break;

                        cPos = c;
                        rPos = r;
                    }

                }
                if (occurrence == 1)
                {
                    // --number is confirmed---
                    actual[cPos][rPos] = n;
                    changes = true;
                    // ---accumulate the total score---
                    totalscore += 2;
                }

            }
        }
        return changes;
    }

    // =========================================================
    // Look for Lone Rangers in Columns
    // =========================================================
    private boolean lookForLoneRangersinColumns() throws Exception {
        boolean changes = false;
        int occurrence ;
        int cPos = 0, rPos = 0;
        for (int c = 1;c <= 9;c++)
        {
            for (int n = 1;n <= 9;n++)
            {
                // ----check by column----
                occurrence = 0;
                for (int r = 1;r <= 9;r++)
                {
                    if (actual[c][r] == 0 && possible[c][r].contains(String.valueOf(n)))
                    {
                        occurrence += 1;
                        // ---if multiple occurrence, not a lone ranger anymore
                        if (occurrence > 1)
                            break;

                        cPos = c;
                        rPos = r;
                    }

                }
                if (occurrence == 1)
                {
                    // --number is confirmed---
                    actual[cPos][rPos] = n;
                    changes = true;
                    // ---accumulate the total score---
                    totalscore += 2;
                }

            }
        }
        return changes;
    }

    // ==================================================
    // Look for Twins in Minigrids
    // ==================================================
    private boolean lookForTwinsinMinigrids() throws Exception {
        boolean changes = false;
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++)
            {
                // ---look for twins in each cell---
                // ---if two possible values, check for twins---
                if (actual[c][r] == 0 && possible[c][r].length() == 2)
                {
                    // ---scan by the mini-grid that the current cell is in---
                    int startC , startR ;
                    startC = c - (c - 1) % 3;
                    startR = r - (r - 1) % 3;
                    for (int rr = startR;rr <= startR + 2;rr++)
                    {
                        for (int cc = startC;cc <= startC + 2;cc++)
                        {
                            // ---for cells other than the pair of twins---
                            if (!(cc == c && rr == r) && possible[cc][rr] == possible[c][r])
                            {
                                for (int rrr = startR;rrr <= startR + 2;rrr++)
                                {
                                    for (int ccc = startC;ccc <= startC + 2;ccc++)
                                    {
                                        // ---remove the twins from all the other possible
                                        // values in the minigrid---
                                        if (actual[ccc][rrr] == 0 && possible[ccc][rrr] != possible[c][r])
                                        {
                                            // ---save a copy of the original
                                            // possible values (twins)---
                                            String original_possible = possible[ccc][rrr];
                                            // ---remove first twin number from
                                            // possible values---
                                            possible[ccc][rrr] = possible[ccc][rrr].replace(Character.toString(possible[c][r].charAt(0)), "");
                                            // ---remove second twin number from
                                            // possible values---
                                            possible[ccc][rrr] = possible[ccc][rrr].replace(Character.toString(possible[c][r].charAt(1)), "");
                                            // ---if the possible values are
                                            // modified, then set the changes
                                            // variable to true to indicate
                                            // that the possible values of cells
                                            // in the minigrid have been modified---
                                            if (original_possible !=possible[ccc][rrr])
                                            changes = true;

                                            // ---if possible value reduces to
                                            // empty string, then the user has
                                            // placed a move that results in
                                            // the puzzle not solvable---
                                            if (possible[ccc][rrr] == "")
                                            throw new Exception("Invalid Move");

                                            // ---if left with 1 possible value
                                            // for the current cell, cell is
                                            // confirmed---
                                            if (possible[ccc][rrr].length() == 1)
                                            {
                                                actual[ccc][rrr] = Integer.valueOf(possible[ccc][rrr]);
                                                // ---accumulate the total score---
                                                totalscore += 3;
                                            }

                                        }

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
    // Look for Twins in Rows
    // ==================================================
    private boolean lookForTwinsinRows() throws Exception {
        boolean changes = false;
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++)
            {
                // ---for each row, check each column in the row---
                // ---if two possible values, check for twins---
                if (actual[c][r] == 0 && possible[c][r].length() == 2)
                {
                    for (int cc = c + 1;cc <= 9;cc++)
                    {
                        // --scan columns in this row---
                        if (possible[cc][r] == possible[c][r])
                        {
                            for (int ccc = 1;ccc <= 9;ccc++)
                            {
                                // ---remove the twins from all the other possible
                                // values in the row---
                                if (actual[ccc][r] == 0 && ccc != c && ccc != cc)
                                {
                                    // ---save a copy of the original possible
                                    // values (twins)---
                                    String original_possible = possible[ccc][r];
                                    // ---remove first twin number from possible
                                    // values---
                                    possible[ccc][r] = possible[ccc][r].replace(Character.toString(possible[c][r].charAt(0)), "");
                                    // ---remove second twin number from possible
                                    // values---
                                    possible[ccc][r] = possible[ccc][r].replace(Character.toString(possible[c][r].charAt(1)), "");
                                    // ---if the possible values are modified, then
                                    // set the changes variable to true to indicate
                                    // that the possible values of cells in the
                                    // minigrid have been modified---
                                    if ((original_possible != possible[ccc][r]))
                                    changes = true;

                                    // ---if possible value reduces to empty string,
                                    // then the user has placed a move that results
                                    // in the puzzle not solvable---
                                    if (possible[ccc][r] == "")
                                    throw new Exception("Invalid Move");

                                    // ---if left with 1 possible value for the
                                    // current cell][cell is confirmed---
                                    if (possible[ccc][r].length() == 1)
                                    {
                                        actual[ccc][r] = Integer.valueOf(possible[ccc][r]);
                                        // ---accumulate the total score---
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
    // Look for Twins in Columns
    // ==================================================
    private boolean lookForTwinsinColumns() throws Exception {
        boolean changes = false;
        for (int c = 1;c <= 9;c++)
        {
            for (int r = 1;r <= 9;r++)
            {
                // ---for each column, check each row in the column---
                // ---if two possible values, check for twins---
                if (actual[c][r] == 0 && possible[c][r].length() == 2)
                {
                    for (int rr = r + 1;rr <= 9;rr++)
                    {
                        // --scan rows in this column---
                        if (possible[c][rr] == possible[c][r])
                        {
                            for (int rrr = 1;rrr <= 9;rrr++)
                            {
                                // ---remove the twins from all the other possible
                                // values in the row---
                                if (actual[c][rrr] == 0 && rrr != r && rrr != rr)
                                {
                                    // ---save a copy of the original possible
                                    // values (twins)---
                                    String original_possible = possible[c][rrr];
                                    // ---remove first twin number from possible
                                    // values---
                                    possible[c][rrr] = possible[c][rrr].replace(Character.toString(possible[c][r].charAt(0)), "");
                                    // ---remove second twin number from possible
                                    // values---
                                    possible[c][rrr] = possible[c][rrr].replace(Character.toString(possible[c][r].charAt(1)), "");
                                    // ---if the possible values are modified, then
                                    // set the changes variable to true to indicate
                                    // that the possible values of cells in the
                                    // minigrid have been modified---
                                    if (original_possible !=possible[c][rrr])
                                    changes = true;

                                    // ---if possible value reduces to empty string,
                                    // then the user has placed a move that results
                                    // in the puzzle not solvable---
                                    if (possible[c][rrr] == "")
                                    throw new Exception("Invalid Move");

                                    // ---if left with 1 possible value for the
                                    // current cell, cell is confirmed---
                                    if (possible[c][rrr].length() == 1)
                                    {
                                        actual[c][rrr] = Integer.valueOf(possible[c][rrr]);
                                        // ---accumulate the total score---
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
    // Look for Triplets in Minigrids
    // ==================================================
    private boolean lookForTripletsinMinigrids() throws Exception {
        boolean changes = false;
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++)
            {
                // ---check each cell---
                // --- three possible values; check for triplets---
                if (actual[c][r] == 0 && possible[c][r].length() == 3)
                {
                    // ---first potential triplet found---
                    String tripletsLocation = String.valueOf(c) + String.valueOf(r);
                    // ---scan by mini-grid---
                    int startC , startR ;
                    startC = c - (c - 1) % 3;
                    startR = r - (r - 1) % 3;
                    for (int rr = startR;rr <= startR + 2;rr++)
                    {
                        for (int cc = startC;cc <= startC + 2;cc++)
                        {
                            if (!(cc == c && rr == r) && (possible[cc][rr] == possible[c][r] || possible[cc][rr].length()
                                    == 2 && possible[c][r].contains(Character.toString(possible[cc][rr].charAt(0)))
                                    && possible[c][r].contains(Character.toString(possible[c][r].charAt(0)))))
                            // ---save the coorindates of the triplets
                            tripletsLocation += String.valueOf(cc) + String.valueOf(rr);

                        }
                    }
                    // --found 3 cells as triplets; remove all from the other
                    // cells---
                    if (tripletsLocation.length() == 6)
                    {
                        for (int rrr = startR;rrr <= startR + 2;rrr++)
                        {
                            for (int ccc = startC;ccc <= startC + 2;ccc++)
                            {
                                // ---remove each cell's possible values containing the
                                // triplet---
                                // ---look for the cell that is not part of the
                                // 3 cells found---
                                if (actual[ccc][rrr] == 0 && ccc != Character.getNumericValue(tripletsLocation.charAt(0))
                                        && rrr != Character.getNumericValue(tripletsLocation.charAt(1))
                                        && ccc != Character.getNumericValue(tripletsLocation.charAt(2))
                                        && rrr != Character.getNumericValue(tripletsLocation.charAt(3))
                                        && ccc != Character.getNumericValue(tripletsLocation.charAt(4))
                                        && rrr != Character.getNumericValue(tripletsLocation.charAt(5)) )
                                {
                                    // ---save the original possible values---
                                    String original_possible = possible[ccc][rrr];
                                    // ---remove first triplet number from possible
                                    // values---
                                    possible[ccc][rrr] = possible[ccc][rrr].replace(Character.toString(possible[c][r].charAt(0)), "");
                                    // ---remove second triplet number from possible
                                    // values---
                                    possible[ccc][rrr] = possible[ccc][rrr].replace(Character.toString(possible[c][r].charAt(1)), "");
                                    // ---remove third triplet number from possible
                                    // values---
                                    possible[ccc][rrr] = possible[ccc][rrr].replace(Character.toString(possible[c][r].charAt(2)), "");
                                    // ---if the possible values are modified, then
                                    // set the changes variable to true to indicate
                                    // that the possible values of cells in the
                                    // minigrid have been modified---
                                    if (original_possible != possible[ccc][rrr])
                                    changes = true;

                                    // ---if possible value reduces to empty string,
                                    // then the user has placed a move that results
                                    // in the puzzle not solvable---
                                    if (possible[ccc][rrr] == "")
                                    throw new Exception("Invalid Move");

                                    // ---if left with 1 possible value for the
                                    // current cell, cell is confirmed---
                                    if (possible[ccc][rrr].length() == 1)
                                    {
                                        actual[ccc][rrr] = Integer.valueOf(possible[ccc][rrr]);
                                        // ---accumulate the total score---
                                        totalscore += 4;
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
    // Look for Triplets in Rows
    // ==================================================
    private boolean lookForTripletsinRows() throws Exception {
        boolean changes = false;
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++)
            {
                // ---for each row, check each column in the row---
                // --- three possible values; check for triplets---
                if (actual[c][r] == 0 && possible[c][r].length() == 3)
                {
                    // ---first potential triplet found---
                    String tripletsLocation = String.valueOf(c) + String.valueOf(r);
                    for (int cc = 1;cc <= 9;cc++)
                    {
                        // ---scans columns in this row---
                        // ---look for other triplets---
                        if (cc != c && (possible[cc][r] == possible[c][r] || possible[cc][r].length() == 2
                                && possible[c][r].contains(Character.toString(possible[cc][r].charAt(0))) && possible[c][r].contains(Character.toString(possible[cc][r].charAt(1)))))
                        // ---save the coorindates of the triplet---
                        tripletsLocation += String.valueOf(cc) + String.valueOf(r);

                    }
                    // --found 3 cells as triplets; remove all from the other
                    // cells---
                    if (tripletsLocation.length() == 6)
                    {
                        for (int ccc = 1;ccc <= 9;ccc++)
                        {
                            // ---remove each cell's possible values containing the
                            // triplet---
                            if (actual[ccc][r] == 0 && ccc != Character.getNumericValue(tripletsLocation.charAt(0))
                                    && ccc != Character.getNumericValue(tripletsLocation.charAt(2))
                                    && ccc != Character.getNumericValue(tripletsLocation.charAt(4)) )
                            {
                                // ---save the original possible values---
                                String original_possible = possible[ccc][r];
                                // ---remove first triplet number from possible
                                // values---
                                possible[ccc][r] = possible[ccc][r].replace(Character.toString(possible[c][r].charAt(0)), "");
                                // ---remove second triplet number from possible
                                // values---
                                possible[ccc][r] = possible[ccc][r].replace(Character.toString(possible[c][r].charAt(1)), "");
                                // ---remove third triplet number from possible
                                // values---
                                possible[ccc][r] = possible[ccc][r].replace(Character.toString(possible[c][r].charAt(2)), "");
                                // ---if the possible values are modified][then set
                                // the changes variable to true to indicate that
                                // the possible values of cells in the minigrid
                                // have been modified---
                                if (original_possible != possible[ccc][r])
                                changes = true;

                                // ---if possible value reduces to empty string,
                                // then the user has placed a move that results
                                // in the puzzle not solvable---
                                if (possible[ccc][r] == "")
                                throw new Exception("Invalid Move");

                                // ---if left with 1 possible value for the current
                                // cell, cell is confirmed---
                                if (possible[ccc][r].length() == 1)
                                {
                                    actual[ccc][r] = Integer.valueOf(possible[ccc][r]);
                                    // ---accumulate the total score---
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
    // Look for Triplets in Columns
    // ==================================================
    private boolean lookForTripletsinColumns() throws Exception {
        boolean changes = false;
        for (int c = 1;c <= 9;c++)
        {
            for (int r = 1;r <= 9;r++)
            {
                // ---for each column, check each row in the column---
                // --- three possible values; check for triplets---
                if (actual[c][r] == 0 && possible[c][r].length() == 3)
                {
                    // ---first potential triplet found---
                    String tripletsLocation = String.valueOf(c) + String.valueOf(r);
                    for (int rr = 1;rr <= 9;rr++)
                    {
                        // ---scans rows in this column---
                        if (rr != r && (possible[c][rr] == possible[c][r] || possible[c][rr].length() == 2 &&
                                possible[c][r].contains(Character.toString(possible[c][rr].charAt(0)))
                                && possible[c][r].contains(Character.toString(possible[c][rr].charAt(1)))))
                        // ---save the coorindates of the triplet---
                        tripletsLocation += String.valueOf(c) + String.valueOf(rr);

                    }
                    // --found 3 cells as triplets; remove all from the other cells---
                    if (tripletsLocation.length() == 6)
                    {
                        for (int rrr = 1;rrr <= 9;rrr++)
                        {
                            // ---remove each cell's possible values containing the
                            // triplet---
                            if (actual[c][rrr] == 0 && rrr != Character.getNumericValue(tripletsLocation.charAt(1))
                                    && rrr != Character.getNumericValue(tripletsLocation.charAt(3))
                                    && rrr != Character.getNumericValue(tripletsLocation.charAt(5)))
                            {
                                // ---save the original possible values---
                                String original_possible = possible[c][rrr];
                                // ---remove first triplet number from possible
                                // values---
                                possible[c][rrr] = possible[c][rrr].replace(possible[c][r].charAt(0), Character.MIN_VALUE);
                                // ---remove second triplet number from possible
                                // values---
                                possible[c][rrr] = possible[c][rrr].replace(possible[c][r].charAt(1), Character.MIN_VALUE);
                                // ---remove third triplet number from possible
                                // values---
                                possible[c][rrr] = possible[c][rrr].replace(possible[c][r].charAt(2), Character.MIN_VALUE);
                                // ---if the possible values are modified, then set
                                // the changes variable to true to indicate that
                                // the possible values of cells in the minigrid
                                // have been modified---
                                if (original_possible!= possible[c][rrr])
                                changes = true;

                                // ---if possible value reduces to empty string,
                                // then the user has placed a move that results
                                // in the puzzle not solvable---
                                if (possible[c][rrr] == "")
                                throw new Exception("Invalid Move");

                                // ---if left with 1 possible value for the current
                                // cell, cell is confirmed---
                                if (possible[c][rrr].length() == 1)
                                {
                                    actual[c][rrr] = Integer.valueOf(possible[c][rrr]);
                                    // ---accumulate the total score---
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

    // =========================================================
    // Find the cell with the small number of possible values
    // =========================================================
    private int[] findCellWithFewestPossibleValues(int col, int row) throws Exception {
        int[] tab = new int[2];
        int min = 10;
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++)
            {
                if (actual[c][r] == 0 && possible[c][r].length() < min)
                {
                    min = possible[c][r].length();
                    tab[0] = c;
                    tab[1] = r;
                }

            }
        }
        return tab;
    }

    // ==================================================
    // Solve puzzle by brute force
    // ==================================================
    private void solvePuzzleByBruteForce() throws Exception {
        int c = 0, r = 0;
        int[] tab;
        // ---accumulate the total score---
        totalscore += 5;
        // ---find out which cell has the smallest number of possible values---

        tab = findCellWithFewestPossibleValues(c,r);
        c = tab[0];
        r = tab[1];
        // ---get the possible values for the chosen cell---
        String possibleValues = possible[c][r];
        // ---randomize the possible values----
        possibleValues = randomizeThePossibleValues(possibleValues);
        // -------------------
        // ---push the actual and possible stacks into the stack---
        int[][] actualClone = new int[10][10];
        String[][] possibleClone = new String[10][10];
        for (int rr = 1;rr <= 9;rr++)
        {
            for (int cc = 1;cc <= 9;cc++)
            {
                actualClone[cc][rr]=actual[cc][rr];
                possibleClone[cc][rr]=possible[cc][rr];
            }
        }
        ActualStack.push(actualClone);
        PossibleStack.push(possibleClone);
        for (int i = 0;i <= possibleValues.length() - 1;i++)
        {
            // ---select one value and try---
            actual[c][r] = Character.getNumericValue(possibleValues.charAt(i));
            try
            {
                if (solvePuzzle())
                {
                    // ---if the puzzle is solved, the recursion can stop now---
                    BruteForceStop = true;
                    return ;
                }
                else
                {
                    // ---no problem with current selection, proceed with next
                    // cell---
                    solvePuzzleByBruteForce();
                    if (BruteForceStop)
                        return ;

                }
            }
            catch (Exception ex)
            {
                // ---accumulate the total score---
                totalscore += 5;
                actual = ActualStack.pop();
                possible = PossibleStack.pop();
            }

        }
    }

    // ==================================================
    // Check if the puzzle is solved
    // ==================================================
    private boolean isPuzzleSolved() throws Exception {
        String pattern = new String();
        int r , c ;
        for (r = 1;r <= 9;r++)
        {
            // ---check row by row---
            pattern = "123456789";
            for (c = 1;c <= 9;c++)
                pattern = pattern.replace(String.valueOf(actual[c][r]), "");
            if (pattern.length() > 0)
                return false;

        }
        for (c = 1;c <= 9;c++)
        {
            // ---check col by col---
            pattern = "123456789";
            for (r = 1;r <= 9;r++)
                pattern = pattern.replace(String.valueOf(actual[c][r]), "");
            if (pattern.length() > 0)
                return false;

        }
        for (c = 1;c <= 9;c += 3)
        {
            // ---check by minigrid---
            pattern = "123456789";
            for (r = 1;r <= 9;r += 3)
            {
                for (int cc = 0;cc <= 2;cc++)
                {
                    for (int rr = 0;rr <= 2;rr++)
                        pattern = pattern.replace(String.valueOf(actual[c + cc][r + rr]), "");
                }
            }
            if (pattern.length() > 0)
                return false;

        }
        return true;
    }

    // =========================================================
    // Randomly swap the list of possible values
    // =========================================================
    private String randomizeThePossibleValues(String str) throws Exception {
        char[] s;
        int i , j ;
        char temp;
        Random rnd = new Random();
        s = str.toCharArray();
        for (i = 0;i < str.length() - 1;i++)
        {
            j = Math.abs((str.length() - i + 1) * rnd.nextInt() + i) % str.length();
            // ---swap the chars---
            temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
        return new String(s);
    }

    // ============================================================
    // Generate a random number between the specified range
    // ============================================================
    private int randomNumber(int min, int max) throws Exception {
        Random r = new Random();
        return r.nextInt((max-min)) + min;
    }

    //for ints
    // ============================================================
    // Get Puzzle
    // ============================================================
    public int[] getPuzzle(int level) throws Exception {
        totalscore = 0;
        int[] result;
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
    // Create empty cells in the grid
    // ============================================================
    private void createEmptyCells(int empty) throws Exception {
        int c , r ;
        // ----choose random locations for empty cells----
        String[] emptyCells = new String[empty - 1 + 1];
        for (int i = 0;i <= empty / 2;i++)
        {
            boolean duplicate ;
            do
            {
                duplicate = false;
                do
                {
                    // ---get a cell in the first half of the grid
                    c = randomNumber(1,9);
                    r = randomNumber(1,5);
                }
                while (r == 5 & c > 5);
                for (int j = 0;j <= i;j++)
                {
                    // ---if cell is already selected to be empty
                    if (emptyCells[j] == c +String.valueOf(r))
                    {
                        duplicate = true;
                        break;
                    }

                }
                if (!duplicate)
                {
                    // ---set the empty cell---
                    emptyCells[i] = String.valueOf(c) + String.valueOf(r);
                    actual[c][r] = 0;
                    possible[c][r] = "";
                    // ---reflect the top half of the grid and make it symmetrical---
                    emptyCells[empty - 1 - i] = String.valueOf(10 - c) + toString().valueOf(10 - r);
                    actual[10 - c][10 - r] = 0;
                    possible[10 - c][10 - r] = "";
                }

            }
            while (duplicate);
        }
    }

    // ============================================================
    // Generate a new Sudoku puzzle
    // ============================================================
    private int[] generateNewPuzzle(int level) throws Exception {

        int[] str = new int[81];
        int numberofemptycells = 0;
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++)
            {
                // ---initialize the entire board---
                actual[c][r] = 0;
                possible[c][r] = "";
            }
        }
        // ---clear the stacks---
        ActualStack.clear();
        PossibleStack.clear();
        try
        {
            // ---populate the board with numbers by solving an empty grid---
            // --- use logical methods to setup the grid first ---
            if (!solvePuzzle())
                // ---then use brute-force---
                solvePuzzleByBruteForce();

        }
        catch (Exception ex)
        {
            return null;
        }

        // ---just in case an error occured, return an empty string---
        // ---make a backup copy of the Actual array---
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++)
            {
                actual_backup[c][r] = actual[c][r];
            }
        }
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
        // ---clear the stacks that are used in brute-force elimination ---
        ActualStack.clear();
        PossibleStack.clear();
        BruteForceStop = false;
        // ----create empty cells----
        createEmptyCells(numberofemptycells);
        // ---convert the values in the actual array to a string---
        for (int r = 1;r <= 9;r++)
        {
            for (int c = 1;c <= 9;c++) {
                str[(r-1) * 9 + (c-1)] = actual[c][r];
            }
        }
        // ---verify the puzzle has only one solution---
        int tries = 0;
        do
        {
            totalscore = 0;
            try
            {
                if (!solvePuzzle())
                {
                    // ---if puzzle is not solved and
                    // this is a level 1 to 3 puzzle---
                    if (level < 4)
                    {
                        // ---choose another pair of cells to empty---
                        str = vacateAnotherPairOfCells(str);
                        tries += 1;
                    }
                    else
                    {
                        // ---level 4 puzzles does not guarantee single
                        // solution and potentially need guessing---
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

            // ---puzzle does indeed have 1 solution---
            // ---if too many tries, exit the loop---
            if (tries > 50)
                return null;

        }
        while (true);
        // ==================================================
        // ---return the score as well as the puzzle as a string---
        return str;
    }

    // ============================================================
    // Vacate another pair of cells
    // ============================================================
    private int[] vacateAnotherPairOfCells(int[] str) throws Exception {
        int c, r;
        do {
            // ---look for a pair of cells to restore---
            c = randomNumber(1, 9);
            r = randomNumber(1, 9);
        }
        while (str[c-1 + (r-1)*9] !=0);

        str[(c - 1 + (r - 1) * 9)] = actual_backup[c][r];
        str[(10 - c - 1 + (10 - r - 1)*9)] = actual_backup[10 - c][10 - r];

        do {
            // ---look for another pair of cells to vacate---
            c = randomNumber(1, 9);
            r = randomNumber(1, 9);
        }
        while (str[(c - 1 + (r - 1) * 9)] == 0);
        // ---remove the cell from the str---
        str[(c - 1 + (r - 1) * 9)] = 0;
        str[(10 - c - 1 + (10 - r - 1)*9)] = 0;
        // ---reinitialize the board---
        short counter = 0;
        for (int row = 1; row <= 9; row++) {
            for (int col = 1; col <= 9; col++) {
                if (str[counter] != 0) {
                    actual[col][row] = str[counter];
                    possible[col][row] = String.valueOf(str[counter]);
                } else {
                    actual[col][row] = 0;
                    possible[col][row] = "";
                }
                counter += 1;
            }
        }
        int[] result = new int[81];
        for(int rr=0;rr<=2;rr++)
        {
           result[rr] = str[rr];
        }
        return result;
    }
}



