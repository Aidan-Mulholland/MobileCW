package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditGoalActivity extends AppCompatActivity {

    private String type;
    private Bundle extras;
    private int parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        extras = getIntent().getExtras();
        parent = extras.getInt("parent");
        System.out.println(parent);

        EditText name = findViewById(R.id.goal_name);
        EditText description = findViewById(R.id.goal_description);
        Spinner dropdown = findViewById(R.id.spinner);

        // Populate dropdown with goal types from the type table
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        Cursor cursor = getContentResolver().query(
                DatabaseContract.Type_Table.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(DatabaseContract.Type_Table.COLUMN_TYPE));
                spinnerAdapter.add(type);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Hide spinner if not editing a parent goal
        if (parent != 0) {
            dropdown.setVisibility(View.INVISIBLE);
        }

        name.setText(extras.getString("name"));
        description.setText(extras.getString("description"));

        Button btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(view -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_NAME, name.getText().toString());
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_DESCRIPTION, description.getText().toString());
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_PROGRESS, 0);
            // If parent is assigned in the intent bundle then assign this value in the content values
            if (parent != 0) {
                contentValues.put(DatabaseContract.Goal_Table.COLUMN_TYPE, extras.getString("type"));
                contentValues.put(DatabaseContract.Goal_Table.COLUMN_PARENT, parent);
            } else {
                contentValues.put(DatabaseContract.Goal_Table.COLUMN_TYPE, type);
            }
            getContentResolver().insert(DatabaseContract.Goal_Table.CONTENT_URI, contentValues);
            Intent intent = new Intent(EditGoalActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}