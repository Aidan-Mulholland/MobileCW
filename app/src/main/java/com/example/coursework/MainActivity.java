package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.os.Bundle;

import android.content.Intent;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private RecyclerView goalsView;
    private RecyclerView tagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalsView = findViewById(R.id.goalsView);
        tagsView = findViewById(R.id.tagsView);

        // Get goal types and populate recycler view with these options
        Cursor cursor = getContentResolver().query(DatabaseContract.Type_Table.CONTENT_URI, null, null, null, null);
        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> typeColours = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Create alarm object for each row in cursor
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(DatabaseContract.Type_Table.COLUMN_TYPE));
                @SuppressLint("Range") String colour = cursor.getString(cursor.getColumnIndex(DatabaseContract.Type_Table.COLUMN_COLOUR));
                types.add(type);
                typeColours.add(colour);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Type_RecyclerViewAdapter adapter = new Type_RecyclerViewAdapter(this, types, typeColours);
        tagsView.setAdapter(adapter);



        // Populate goal list view with goals
        updateGoalListView();

        // Add button onclick listener
        FloatingActionButton addGoal = findViewById(R.id.addGoal);
        addGoal.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateGoalActivity.class);
            startActivity(intent);
        });

        // Bottom navigation code to switch between activities
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.getMenu().findItem(R.id.activity_main).setChecked(true);
        nav.setOnItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.sleep) {
                intent = new Intent(MainActivity.this, SleepActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.journal) {
                intent = new Intent(MainActivity.this, JournalActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.guide) {
                intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            } else {
                return false;
            }
            return true;
        });
    }

    @SuppressLint("Range")
    public void updateGoalListView() {
        Cursor cursorA = getContentResolver().query(DatabaseContract.Goal_Table.CONTENT_URI, null, null, null, null);
        Cursor cursorB = getContentResolver().query(DatabaseContract.Type_Table.CONTENT_URI, null, null, null, null);
        ArrayList<GoalModel> goals = new ArrayList<>();
        ArrayList<String> colours = new ArrayList<>();
        if (cursorA.moveToFirst()) {
            do {
                // Create goal for each goal in the database
                int goalID = cursorA.getInt(cursorA.getColumnIndex(DatabaseContract.Goal_Table._ID));
                String name = cursorA.getString(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_NAME));
                String description = cursorA.getString(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_DESCRIPTION));
                int progress = cursorA.getInt(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_PROGRESS));
                String type = cursorA.getString(cursorA.getColumnIndex(DatabaseContract.Goal_Table.COLUMN_TYPE));

                // Add each alarm model to arraylist
                GoalModel goal = new GoalModel(goalID, name, description, progress, type);
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
        Goal_RecyclerViewAdapter adapter = new Goal_RecyclerViewAdapter(this, goals, colours);
        goalsView.setAdapter(adapter);
    }
}