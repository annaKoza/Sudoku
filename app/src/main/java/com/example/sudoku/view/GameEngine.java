package com.example.sudoku.view;

import com.example.sudoku.com.example.sudoku.game.Cell;
import com.example.sudoku.com.example.sudoku.game.CellCollection;
import com.example.sudoku.view.sudokuGrid.GameGrid;

import android.content.Context;

public class GameEngine {
	private static GameEngine instance;
	private GameGrid grid = null;
	private int selectedPosX = -1, selectedPosY = -1;
	private CellCollection model;

	private GameEngine(){}
	
	public static GameEngine getInstance(){
		if( instance == null ){
			instance = new GameEngine();
		}
		return instance;
	}
	
	public void createGrid( Context context, int level ){

		model = CellCollection.createEmptyGrid();

		try
		{
			Cell[][] puzzle = model.getPuzzle(level);
			grid = new GameGrid(context);
			grid.setGrid(puzzle);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public  void createEmptyGrid(Context context)
	{
		model = CellCollection.createEmptyGrid();
		try
		{
			grid = new GameGrid(context);
			grid.setGrid(model.getCells());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public GameGrid getGrid(){
	    return grid;
	}
	
	public void setSelectedPosition( int x , int y ){
		selectedPosX = x;
		selectedPosY = y;
	}
	
	public void setNumber( int number ){
		if( selectedPosX != -1 && selectedPosY != -1 ){
			grid.setItem(selectedPosX,selectedPosY,number);
			model.getCellOnPosition(selectedPosX,selectedPosY).setValue(number);
		}
	}

	public  boolean checkGame()
    {
        return model.isPuzzleSolved();
    }
	public  boolean checkCurrentState()
	{
		return true;
	//	return model.validateCurrentGrid();
	}
    public void resetGame() {
        grid.setGrid(model.resetPuzzle());
    }

	public boolean solve() {
			if(model.isPuzzleSolvable()) {
				boolean result = model.solveEmpty();
			    grid.setGrid(model.getCells());
				return result;
			}
			else
				return false;
	}
}
