package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateJournalActivity extends AppCompatActivity {

    private EditText journalEntry;
    private Button submit;
    private ImageButton back;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journal);

        journalEntry = findViewById(R.id.journalEntry);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.backButton);
        Bundle extras = getIntent().getExtras();

        submit.setOnClickListener(view -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.Journal_Table.COLUMN_DATE, extras.getString("date"));
            contentValues.put(DatabaseContract.Journal_Table.COLUMN_JOURNAL, journalEntry.getText().toString());
            getContentResolver().update(DatabaseContract.Journal_Table.CONTENT_URI, contentValues, DatabaseContract.Journal_Table.COLUMN_DATE + "='" + extras.getString("date")+"'", null);

            // Start journal activity
            Intent intent = new Intent(CreateJournalActivity.this, JournalActivity.class);
            startActivity(intent);
        });

        back.setOnClickListener(view -> {
            Intent intent =  new Intent(CreateJournalActivity.this, JournalActivity.class);
            startActivity(intent);
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        Bundle extras = getIntent().getExtras();
        SharedPreferences myPref = getSharedPreferences(getString(R.string.journal_preferences), MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myPref.edit();
        myEditor.clear();
        myEditor.putString(extras.getString("date"), journalEntry.getText().toString());
        myEditor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        SharedPreferences myPref = getSharedPreferences(getString(R.string.journal_preferences), MODE_PRIVATE);
        journalEntry.setText(myPref.getString(extras.getString("date"), null));
    }

    @SuppressLint("Range")
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (journalEntry.getText().toString().equals("")) {
            // Query for a journal on given day and populate with data if a journal exists
            Cursor cursor = getContentResolver(). query(
                    DatabaseContract.Journal_Table.CONTENT_URI,
                    null,
                    DatabaseContract.Journal_Table.COLUMN_DATE + " = '" + extras.getString("date")+"'",
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                journalEntry.setText(cursor.getString(cursor.getColumnIndex(DatabaseContract.Journal_Table.COLUMN_JOURNAL)));
            }
            cursor.close();
        }

    }
}