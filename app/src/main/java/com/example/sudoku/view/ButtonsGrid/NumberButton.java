package com.example.sudoku.view.ButtonsGrid;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.sudoku.view.GameEngine;

public class NumberButton extends android.support.v7.widget.AppCompatButton implements OnClickListener{

	private int number;
	
	public NumberButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		GameEngine.getInstance().setNumber(number);
		setNumber(number);
	}
	
	public void setNumber(int number){
		this.number = number;
	}
}
