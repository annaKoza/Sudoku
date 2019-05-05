package com.example.sudoku.view.sudokuGrid;

import android.content.Context;
import android.view.View;

public class BaseSudokuCell extends View {
	private int value;
	private boolean modifiable = true;
	private boolean isSelected;
	private boolean isValid;

	public BaseSudokuCell(Context context) {
		super(context);
		isValid = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
	
	public void setNotModifiable(){
		modifiable = false;
	}
	
	public void setInitValue(int value){
		this.value = value;
		invalidate();
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue( int value ){
		if( modifiable ){
			this.value = value;
		}
		invalidate();
	}

	public boolean getModifiable()
	{
		return modifiable;
	}

	public boolean getIsSelected()
	{
		return  isSelected;
	}
	public void setIsSelected(boolean value) {
		isSelected = value;
		invalidate();
	}

	public boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(boolean value) {
		isValid = value;
		invalidate();
	}
}
