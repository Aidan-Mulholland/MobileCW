package com.example.coursework;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import android.content.Intent;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class GoalActivity extends AppCompatActivity implements GoalRecyclerInterface {

    private RecyclerView goalsView;
    private ArrayList<GoalModel> goals;
    private ArrayList<String> colours;
    private TextView textView;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        extras = getIntent().getExtras();

        goalsView = findViewById(R.id.goalsView);
        textView = findViewById(R.id.goalname);
        textView.setText(extras.getString("name"));


        FloatingActionButton addGoal = findViewById(R.id.addGoal);
        addGoal.setOnClickListener(view -> {
            Intent intent = new Intent(GoalActivity.this, CreateGoalActivity.class);
            intent.putExtra("parent", extras.getInt("id"));
            intent.putExtra("type", extras.getString("type"));
            startActivity(intent);
        });

        // Bottom navigation code to switch between activities
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.getMenu().findItem(R.id.activity_main).setChecked(true);
        nav.setOnItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.sleep) {
                intent = new Intent(GoalActivity.this, SleepActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.activity_main){
                intent = new Intent(GoalActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.journal) {
                intent = new Intent(GoalActivity.this, JournalActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.guide) {
                intent = new Intent(GoalActivity.this, GuideActivity.class);
                startActivity(intent);
            } else {
                return false;
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Populate goal list view with goals
        updateGoalRecyclerView();
    }

    @Override
    protected void onPause() {
        // Clear adapter lists to clear up memory
        super.onPause();
        goals.clear();
        colours.clear();
    }

    @SuppressLint("Range")
    public void updateGoalRecyclerView() {
        Cursor cursorA = getContentResolver().query(DatabaseContract.Goal_Table.CONTENT_URI, null,  DatabaseContract.Goal_Table.COLUMN_PARENT + "=" + extras.getInt("id"), null, null);
        Cursor cursorB = getContentResolver().query(DatabaseContract.Type_Table.CONTENT_URI, null, null, null, null);
        goals = new ArrayList<>();
        colours = new ArrayList<>();
        if (cursorA.moveToFirst()) {
            do {
                // Create goal for each goal in the database
                int goalID = cursorA.getInt(cursorA.getColumnIndex(DatabaseContract.Goal_Table._ID));
                String name = cursorA.getString(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_NAME));
                String description = cursorA.getString(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_DESCRIPTION));
                int progress = cursorA.getInt(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_PROGRESS));
                String type = cursorA.getString(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_TYPE));
                int parent = cursorA.getInt(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_PARENT));

                // Add each alarm model to arraylist
                GoalModel goal = new GoalModel(goalID, name, description, progress, type, parent);
                goals.add(goal);
                // Find matching colour for the goal type
                cursorB.moveToFirst();
                do {
                    if (cursorA.getString(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_TYPE)).equals(cursorB.getString(cursorB.getColumnIndex(DatabaseContract.Type_Table.COLUMN_TYPE)))) {
                        colours.add(cursorB.getString(cursorB.getColumnIndex(DatabaseContract.Type_Table.COLUMN_COLOUR)));
                    }
                } while (cursorB.moveToNext());
            } while (cursorA.moveToNext());
        }
        cursorA.close();
        cursorB.close();
        Goal_RecyclerViewAdapter adapter = new Goal_RecyclerViewAdapter(this, goals, colours, this);
        goalsView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onLongClick(int position) {
        GoalModel goal = goals.get(position);

        Intent intent = new Intent(GoalActivity.this, EditGoalActivity.class);
        intent.putExtra("id", goal.getId());
        intent.putExtra("name", goal.getName());
        intent.putExtra("description", goal.getDescription());
        intent.putExtra("parent", goal.getParent());
        intent.putExtra("type", goal.getType());
        startActivity(intent);

        return false;
    }
}