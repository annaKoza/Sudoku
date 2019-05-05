package com.example.sudoku.com.example.sudoku.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Cell implements Cloneable {
    private final CellGroup row;
    private final CellGroup column;
    private final CellGroup sector;
    private final Position position;
    private boolean isValid;
    private boolean isEditable;
    private List<Integer> possibleValues;
    private int value;
    private Stack<List<Integer>> possibleValuesBackup;
    private Stack<Integer> valueBackup;

    public Cell(Position position, CellGroup row, CellGroup column, CellGroup sector) {
        this.row = row;
        this.column = column;
        this.sector = sector;
        this.position = position;
        isEditable = true;
        isValid = true;
        possibleValues = new ArrayList<>();
        possibleValuesBackup = new Stack<>();
        valueBackup = new Stack<>();
    }

    public Position getPosition()
    {
        return position;
    }

    public boolean checkIfPossibleValueListIsTheSame(List<Integer> otherList)
    {
        if(otherList.size() != possibleValues.size() ) return false;
        return possibleValues.containsAll(otherList);
    }

    public boolean checkIfContainsPossibleValue(int value)
    {
        return possibleValues.contains(value);
    }

    public boolean checkIfContainsPossibleValue(List<Integer> values)
    {
        return possibleValues.containsAll(values);
    }

    public List<Integer> getPossibleValues()
    {
        return possibleValues;
    }

    public int getPossibleValuesCount()
    {
        return  possibleValues.size();
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        if (value < 0 || value > 9)
            throw new InvalidParameterException(String.format("Value in %s is not valid", position));
        else
            this.value = value;
    }

    public boolean isEditable() {
        return isEditable;
    }

    void setEditable(boolean editable) {
        isEditable = editable;
    }

    public boolean isValid() {
        return isValid;
    }

    void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean updateValueIfReady()
    {
        if(possibleValues.size() == 1)
        {
            value = possibleValues.get(0);
            return true;
        }
        return false;
    }

    public void calculatePossibleValues() throws Exception {

        if (possibleValues.isEmpty())
        {
            possibleValues.add(1);
            possibleValues.add(2);
            possibleValues.add(3);
            possibleValues.add(4);
            possibleValues.add(5);
            possibleValues.add(6);
            possibleValues.add(7);
            possibleValues.add(8);
            possibleValues.add(9);
        }

        // ---Step (1) check by column---
        for (Cell cell: column.getCells())
        {
            if (cell.getValue() != 0)
                this.possibleValues.remove(Integer.valueOf(cell.getValue()));
        }
        // ---Step (2) check by row---
        for (Cell cell: row.getCells())
        {
            if (cell.getValue() != 0)
                this.possibleValues.remove(Integer.valueOf(cell.getValue()));
        }
        // ---Step (2) check by sector---
        for (Cell cell: sector.getCells())
        {
            if (cell.getValue() != 0)
                this.possibleValues.remove(Integer.valueOf(cell.getValue()));
        }

        if (possibleValues.isEmpty())
            throw new Exception(String.format("no possible values in cell with position %s", position));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Cell cell = new Cell(this.position, this.row,this.column,this.sector);
        cell.value = this.value;
        cell.isValid = this.isValid;
        cell.possibleValues = new ArrayList<>(this.possibleValues);
        cell.isEditable = this.isEditable;
        return cell;
    }

    void SaveState() {
        valueBackup.push(value);
        possibleValuesBackup.push(new ArrayList<>(possibleValues));
    }

    void LoadState()
    {
        value = valueBackup.pop();
        possibleValues = possibleValuesBackup.pop();
    }

    void ResetCell() {
        value = 0;
        possibleValues = new ArrayList<>();
        possibleValuesBackup = new Stack<>();
        valueBackup = new Stack<>();
    }

    void ClearHistory() {
        possibleValuesBackup = new Stack<>();
        valueBackup = new Stack<>();
    }

    public void CopyCell(Cell cell) {
        value = cell.value;
        possibleValues = new ArrayList<>(cell.possibleValues);
    }

    List<Integer> getRandomizedPossibleValues()
    {
        List<Integer> randomValues = new ArrayList<>(possibleValues);
        Collections.shuffle(randomValues);
        return randomValues;
    }
}