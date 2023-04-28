package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    private Bundle extras;
    private int parent;
    private EditText name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);
        extras = getIntent().getExtras();
        parent = extras.getInt("parent");

        name = findViewById(R.id.goal_name);
        description = findViewById(R.id.goal_description);
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

        // Attempt to get the parent of the goal
        // If parent is not set in the bundle then this defaults to -1
        // If not equal to -1 then the goal has a parent and so the sub goal should not have a dropdown to select type
        // Type is defaulted to the parent goal type
        if (parent != -1) {
            dropdown.setVisibility(View.INVISIBLE);
        }

        Button btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(view -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_NAME, name.getText().toString());
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_DESCRIPTION, description.getText().toString());
            contentValues.put(DatabaseContract.Goal_Table.COLUMN_PROGRESS, 0);
            // If parent is assigned in the intent bundle then assign this value in the content values
            if (parent != -1) {
                contentValues.put(DatabaseContract.Goal_Table.COLUMN_TYPE, extras.getString("type"));
                contentValues.put(DatabaseContract.Goal_Table.COLUMN_PARENT, parent);
            } else {
                contentValues.put(DatabaseContract.Goal_Table.COLUMN_TYPE, type);
            }
            getContentResolver().insert(DatabaseContract.Goal_Table.CONTENT_URI, contentValues);
            Intent intent = new Intent(CreateGoalActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences myPref = getSharedPreferences(getString(R.string.goal_preferences), MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPref.edit();
        myEditor.clear();
        myEditor.putString("name", name.getText().toString());
        myEditor.putString("description", description.getText().toString());
        myEditor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences myPref = getSharedPreferences(getString(R.string.goal_preferences), MODE_PRIVATE);
        name.setText(myPref.getString("name", null));
        description.setText(myPref.getString("description", null));
    }
}