package com.example.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudoku.view.GameEngine;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private View layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(this);

        Intent intent = getIntent();
        Integer level = intent.getIntExtra("level",1);
        GameEngine.getInstance().createGrid(this, level);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.resetButton:
                GameEngine.getInstance().resetGame();
                break;
            case R.id.checkButton:

                LayoutInflater inflater = getLayoutInflater();

               if(GameEngine.getInstance().checkGame())
               {
                   layout = inflater.inflate(R.layout.ok_check_game_custom_toast,
                           (ViewGroup)findViewById(R.id.ok_custom_toast_container));
               }
               else
               {
                   layout = inflater.inflate(R.layout.not_ok_check_game_custom_toast,
                           (ViewGroup)findViewById(R.id.not_ok_custom_toast_container));
               }
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                break;
            default:
                break;
        }
    }
}
