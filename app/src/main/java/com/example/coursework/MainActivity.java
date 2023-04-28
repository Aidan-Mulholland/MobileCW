package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.provider.Settings;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements GoalRecyclerInterface {

    private RecyclerView goalsView, tagsView;
    private ArrayList<GoalModel> goals;
    private ArrayList<String> colours, types, typeColours;
    private Goal_RecyclerViewAdapter goalAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);


        goalsView = findViewById(R.id.goalsView);
        tagsView = findViewById(R.id.tagsView);
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        createNotificationChannel();


        // Add button onclick listener
        FloatingActionButton addGoal = findViewById(R.id.addGoal);
        addGoal.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateGoalActivity.class);
            intent.putExtra("parent", -1);
            startActivity(intent);
        });

        // Bottom navigation code to switch between activities
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

    @Override
    protected void onResume() {
        super.onResume();
        // Repopulate recycler views with respective lists
        updateTypeRecyclerView();
        // Populate goal list view with goals
        updateGoalRecyclerView();
    }

    @Override
    protected void onPause() {
        // Clear adapter lists to clear up memory
        super.onPause();
        types.clear();
        typeColours.clear();
        goals.clear();
        colours.clear();
    }

    @SuppressLint("Range")
    private void updateTypeRecyclerView() {
        // Get goal types and populate recycler view with these options
        Cursor cursor = getContentResolver().query(DatabaseContract.Type_Table.CONTENT_URI, null, null, null, null);
        types = new ArrayList<>();
        typeColours = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // Create alarm object for each row in cursor
                 String type = cursor.getString(cursor.getColumnIndex(DatabaseContract.Type_Table.COLUMN_TYPE));
                 String colour = cursor.getString(cursor.getColumnIndex(DatabaseContract.Type_Table.COLUMN_COLOUR));
                 types.add(type);
                 typeColours.add(colour);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Type_RecyclerViewAdapter typeAdapter = new Type_RecyclerViewAdapter(this, types, typeColours);
        tagsView.setAdapter(typeAdapter);
    }


    @SuppressLint("Range")
    public void updateGoalRecyclerView() {
        Cursor cursorA = getContentResolver().query(DatabaseContract.Goal_Table.CONTENT_URI, null, DatabaseContract.Goal_Table.COLUMN_PARENT + " IS NULL", null, null);
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
        goalAdapter = new Goal_RecyclerViewAdapter(this, goals, colours, this);
        goalsView.setAdapter(goalAdapter);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm";
            String description = "Alarm Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ALARM", name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onItemClick(int position) {
        GoalModel goal = goals.get(position);
        Integer parentMaybeNull = goal.getParent();

        Intent intent = new Intent(MainActivity.this, GoalActivity.class);
        intent.putExtra("id", goal.getId());
        intent.putExtra("name", goal.getName());
        intent.putExtra("type", goal.getType());
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(int position) {
        GoalModel goal = goals.get(position);
        Integer parentMaybeNull = goal.getParent();

        Intent intent = new Intent(MainActivity.this, EditGoalActivity.class);
        intent.putExtra("id", goal.getId());
        intent.putExtra("name", goal.getName());
        intent.putExtra("description", goal.getDescription());
        intent.putExtra("parent", goal.getParent());
        intent.putExtra("type", goal.getType());
        startActivity(intent);
        return false;
    }
}