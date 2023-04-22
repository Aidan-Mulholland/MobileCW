package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CreateGoalActivity extends AppCompatActivity {

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

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
        dropdown.setAdapter(spinnerAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(view -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_NAME, name.getText().toString());
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_DESCRIPTION, description.getText().toString());
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_PROGRESS, 0);
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_TYPE, type);
            getContentResolver().insert(DatabaseContract.Goal_Table.CONTENT_URI, contentValues);
            Intent intent = new Intent(CreateGoalActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}