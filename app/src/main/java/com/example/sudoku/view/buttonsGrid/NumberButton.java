package com.example.sudoku.view.buttonsGrid;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.sudoku.view.commands.IValueCommand;

public class NumberButton extends android.support.v7.widget.AppCompatButton implements OnClickListener{

	private int number;
	private IValueCommand changeValueCommand;
	public NumberButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	public void initialize(IValueCommand changeValueCommand) {
		this.changeValueCommand = changeValueCommand;
	}

	@Override
	public void onClick(View v) {
		changeValueCommand.execute(number);
		setNumber(number);
	}

	public void setNumber(int number){
		this.number = number;
	}
}
