package com.example.sudoku.view.sudokuGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class SudokuCell extends BaseSudokuCell {

	private final Paint invalidPaint;
	private Paint mPaint;
	
	public SudokuCell( Context context ){
		super(context);
		
		mPaint = new Paint();
		invalidPaint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawSelected(canvas);
		drawColor(canvas);
		drawInvalid(canvas);
		drawNumber(canvas);
	}

	private void drawInvalid(Canvas canvas) {
		invalidPaint.setStyle(Paint.Style.FILL);

		if (!getIsValid() && getModifiable()) {
			invalidPaint.setColor(Color.RED);
		} else {
			invalidPaint.setColor(Color.TRANSPARENT);
		}
		canvas.drawRect(20, 20, getWidth() - 21, getHeight() - 21, invalidPaint);
	}

	private void drawSelected(Canvas canvas) {
		mPaint.setStyle(Paint.Style.FILL);

		if(getIsSelected())
		{
			mPaint.setColor(Color.BLUE);
		} else if (!getIsSelected() && getModifiable())
		{
			mPaint.setColor(Color.TRANSPARENT);
		}
		canvas.drawRect(1, 1, getWidth()-2, getHeight()-2, mPaint);
	}

	private void drawColor(Canvas canvas) {
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(getModifiable() ? Color.TRANSPARENT : Color.GRAY);
		canvas.drawRect(1, 1, getWidth()-2, getHeight()-2, mPaint);
	}

	private void drawNumber(Canvas canvas){
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(60);
		mPaint.setStyle(Style.FILL);

		Rect bounds = new Rect();

		mPaint.getTextBounds(String.valueOf(getValue()), 0, String.valueOf(getValue()).length(), bounds);
		if( getValue() != 0 ){
			canvas.drawText(String.valueOf(getValue()), (getWidth() - bounds.width())/2, (getHeight() + bounds.height())/2	, mPaint);
		}
	}

}
