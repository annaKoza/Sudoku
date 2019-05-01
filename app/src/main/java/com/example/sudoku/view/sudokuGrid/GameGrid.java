package com.example.sudoku.view.sudokuGrid;

import android.content.Context;
import com.example.sudoku.com.example.sudoku.game.Cell;
import com.example.sudoku.com.example.sudoku.game.GridSettings;

public class GameGrid {
	private SudokuCell[][] sudoku = new SudokuCell[9][9];
	private Context context;
	
	public GameGrid( Context context ){
		this.context = context;
		for( int x = 0 ; x < 9 ; x++ ){
			for( int y = 0 ; y < 9 ; y++){
				sudoku[x][y] = new SudokuCell(context);
			}
		}
	}

	public SudokuCell[][] getGrid(){
		return sudoku;
	}
	
	public void setGrid( Cell[][] grid ){

		for( int x = 0 ; x < 9 ; x++ ){
			for( int y = 0 ; y < 9 ; y++){
				sudoku[x][y].setInitValue(grid[x][y].getValue());
				if(!grid[x][y].isEditable() ){
					sudoku[x][y].setNotModifiable();
				}
			}
		}
	}
	
	public SudokuCell getItem(int x , int y ){
		return sudoku[x][y];
	}
	
	public SudokuCell getItem( int position ){
		int x = position % 9;
		int y = position / 9;
		
		return sudoku[x][y];
	}
	
	public void setItem( int x , int y , int number ){
		sudoku[x][y].setValue(number);

	}

	public void setItemSelected(int selectedPosX, int selectedPosY) {
		for(int r=0;r< GridSettings.GRID_SIZE;r++)
		{
			for(int c=0;c<GridSettings.GRID_SIZE;c++)
			{
				sudoku[r][c].setIsSelected(false);

			}
		}
		sudoku[selectedPosX][selectedPosY].setIsSelected(true);
	}
}
