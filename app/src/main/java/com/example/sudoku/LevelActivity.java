package com.example.sudoku;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelActivity extends ListActivity {

    public final List<String> LEVELS = new ArrayList<>(Arrays.asList(
            "EASY",
            "MEDIUM",
            "HARD",
            "ADVANCED"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        setListAdapter(new MyAdapter());
        getListView().setItemChecked(0,true);
    }

    public void onStartButtonClick(View v) {
        String obj = (String)getListView().getItemAtPosition(getListView().getCheckedItemPosition());
        Intent startGameIntent = new Intent(this, GameActivity.class);
        startGameIntent.putExtra("level", LEVELS.indexOf(obj)+1);
        startActivity(startGameIntent);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return LEVELS.size();
        }

        @Override
        public String getItem(int position) {
            return LEVELS.get(position);
        }

        @Override
        public long getItemId(int position) {
            return LEVELS.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_level, parent, false);
            }

            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(getItem(position));
            return convertView;
        }
    }
}

