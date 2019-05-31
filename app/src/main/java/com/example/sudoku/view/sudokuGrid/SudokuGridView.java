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

import com.example.sudoku.com.example.sudoku.model.Cell;
import com.example.sudoku.com.example.sudoku.model.GridSettings;
import com.example.sudoku.com.example.sudoku.model.Position;
import com.example.sudoku.viewModel.SudokuPuzzleViewModel;

import java.util.List;

public class SudokuGridView extends GridView{

	private SudokuPuzzleViewModel model;
	private final Paint mSectorLinePaint;
	private final Paint mPaint;
	private float cellHeight;
	private float cellWidth;
	private SudokuCell[][] sudoku = new SudokuCell[9][9];

	public SudokuGridView(final Context context , AttributeSet attrs) {
		super(context,attrs);
		mPaint = new Paint();
		mSectorLinePaint = new Paint();

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				sudoku[x][y] = new SudokuCell(context);
			}
		}

		SudokuGridViewAdapter gridViewAdapter = new SudokuGridViewAdapter();
		setAdapter(gridViewAdapter);

		setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int x = position % GridSettings.GRID_SIZE;
				int y = position / GridSettings.GRID_SIZE;
				setSelectedPosition(x, y);
			}
		});

		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(3);
		mPaint.setStyle(Paint.Style.STROKE);
		mSectorLinePaint.setColor(Color.RED);
		mSectorLinePaint.setStrokeWidth(4);
		mSectorLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	private void setGrid(Cell[][] grid) {

		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				sudoku[x][y].setInitValue(grid[x][y].getValue());
				if (!grid[x][y].isEditable()) {
					sudoku[x][y].setNotModifiable();
				}
			}
		}
	}

	private SudokuCell getItem(int x, int y) {
		return sudoku[x][y];
	}
	private SudokuCell getsItem(int position) {
		int x = position % 9;
		int y = position / 9;

		return getItem(x, y);
	}

	public void setItem(int number) {

		int[] currentPosition = model.getCurrentPosition();
		model.setValueOnCurrentPosition(number);
		sudoku[currentPosition[0]][currentPosition[1]].setValue(number);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawLines(canvas);
	}
	private void drawLines(Canvas canvas) {

		// draw vertical lines
		for (int c = 1; c < 9; c++) {
			float x = (c * cellWidth);
			canvas.drawLine(x, 0, x, getHeight(), mPaint);
		}

		// draw horizontal lines
		for (int r = 0; r <= 9; r++) {
			float y = r * cellHeight;
			canvas.drawLine(0, y, getWidth(), y, mPaint);
		}

		// draw sector (thick) lines
		for (int c = 0; c < 9; c = c + 3) {
			if(c == 0) continue;
			float x = (c * cellWidth);
			canvas.drawRect(x, 0, x , getHeight(), mSectorLinePaint);
		}

		for (int r = 0; r < 9; r = r + 3) {
			if(r == 0) continue;
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

	public void initialize(int level, SudokuPuzzleViewModel model) {
		this.model = model;
		try {
			setGrid(model.getPuzzle(level));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initialize(SudokuPuzzleViewModel model) {
		this.model = model;
		try {
			setGrid(model.getPuzzle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setSelectedPosition(int x, int y) {

		model.setCurrentPosition(x, y);
		for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
			for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
				sudoku[r][c].setIsSelected(false);
			}
		}
		sudoku[x][y].setIsSelected(true);
	}

	public void reset() {
		setGrid(model.resetPuzzle());
	}

	public void solve() {
		setGrid(model.solve());
	}

	public void validate() {

		for (int r = 0; r < GridSettings.GRID_SIZE; r++) {
			for (int c = 0; c < GridSettings.GRID_SIZE; c++) {
				sudoku[r][c].setIsValid(true);
			}
		}
		List<Cell> invalidCells = model.getInvalidCells();
		for (Cell cell : invalidCells) {
			final Position position = cell.getPosition();
			sudoku[position.getRow()][position.getColumn()].setIsValid(false);
		}
	}

	class SudokuGridViewAdapter extends BaseAdapter {

		SudokuGridViewAdapter() {
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
			return getsItem(position);
		}
	}
}
