package com.example.sudoku.view.sudokuGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.sudoku.com.example.sudoku.game.GridSettings;
import com.example.sudoku.view.GameEngine;

public class SudokuGridView extends GridView{

	private final Paint mSectorLinePaint;
	private final Paint mPaint;
	private float cellHeight;
	private float cellWidth;
	private final Context context;

	public SudokuGridView(final Context context , AttributeSet attrs) {
		super(context,attrs);
		mPaint = new Paint();
		mSectorLinePaint = new Paint();

		this.context = context;
		SudokuGridViewAdapter gridViewAdapter = new SudokuGridViewAdapter(context);
		setAdapter(gridViewAdapter);

		setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int x = position % GridSettings.GRID_SIZE;
				int y = position / GridSettings.GRID_SIZE;
				GameEngine.getInstance().setSelectedPosition(x, y);
			}
		});

		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(3);
		mPaint.setStyle(Paint.Style.STROKE);
		mSectorLinePaint.setColor(Color.RED);
		mSectorLinePaint.setStrokeWidth(4);
		mSectorLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLines(canvas);
	}
	private void drawLines(Canvas canvas) {

		// draw vertical lines
		for (int c = 0; c <= 9; c++) {
			float x = (c * cellWidth);
			canvas.drawLine(x, 0, x, getHeight(), mPaint);
		}

		// draw horizontal lines
		for (int r = 0; r <= 9; r++) {
			float y = r * cellHeight;
			canvas.drawLine(0, y, getWidth(), y, mPaint);
		}

		// draw sector (thick) lines
		for (int c = 0; c <= 9; c = c + 3) {
			float x = (c * cellWidth);
			canvas.drawRect(x, 0, x , getHeight(), mSectorLinePaint);
		}

		for (int r = 0; r <= 9; r = r + 3) {
			float y = r * cellHeight;
			canvas.drawRect(0, y , getWidth(), y , mSectorLinePaint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		cellWidth = getColumnWidth();
		cellHeight = getHeight()/9;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	class SudokuGridViewAdapter extends BaseAdapter{
		private Context context;
		
		public SudokuGridViewAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return GridSettings.GRID_SIZE * GridSettings.GRID_SIZE;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return GameEngine.getInstance().getGrid().getItem(position);
		}
	}
}
