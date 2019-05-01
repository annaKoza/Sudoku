package com.example.sudoku.view.sudokuGrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.sudoku.com.example.sudoku.game.GridSettings;
import com.example.sudoku.view.GameEngine;

public class SudokuGridView extends GridView{

	private final Context context;
	
	public SudokuGridView(final Context context , AttributeSet attrs) {
		super(context,attrs);
		
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
