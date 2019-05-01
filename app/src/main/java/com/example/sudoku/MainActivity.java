package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startGameButton = findViewById(R.id.start_button);
        startGameButton.setOnClickListener(this);
        Button solveButton = findViewById(R.id.solve_button);
        solveButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /** Called when the user touches the button */
    public void StartGame(View view) {
        // Do something in response to button click
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.start_button:
                Intent startGameIntent = new Intent(this, LevelActivity.class);
                startActivity(startGameIntent);
                break;
            case R.id.solve_button:
                Intent solveIntent = new Intent(this, SolveActivity.class);
                startActivity(solveIntent);
                break;
                default:
                    break;
        }
    }
}
