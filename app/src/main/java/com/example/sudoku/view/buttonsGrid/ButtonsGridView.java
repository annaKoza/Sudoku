package com.example.sudoku.view.buttonsGrid;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.sudoku.R;
import com.example.sudoku.view.commands.IValueCommand;

public class ButtonsGridView extends GridView {

	private IValueCommand command;

	public ButtonsGridView(Context context, AttributeSet attrs) {
		super(context , attrs);

		ButtonsGridViewAdapter gridViewAdapter = new ButtonsGridViewAdapter(context);
		setAdapter(gridViewAdapter);
	}

	public void initialize(IValueCommand command) {
		this.command = command;
		for (int i = 0; i < getChildCount(); i++) {
			NumberButton button = (NumberButton) getChildAt(i);
			button.initialize(command);
		}
	}

	class ButtonsGridViewAdapter extends BaseAdapter {
		
		private Context context;
		
		public ButtonsGridViewAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			
			if( v == null ){
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				v = inflater.inflate(R.layout.button, parent , false);
				
				NumberButton btn;
				btn = (NumberButton)v;
				btn.initialize(command);
				btn.setTextSize(10);
				btn.setId(position);
				
				if( position != 9 ){
					btn.setText(String.valueOf(position + 1));
					btn.setNumber(position + 1);
				} else {
					btn.setText("DEL");
					btn.setNumber(0);
				}
				return btn;
			}
			return v;
		}
		
	}
}
