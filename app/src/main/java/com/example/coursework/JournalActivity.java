package com.example.coursework;

import static com.example.coursework.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class JournalActivity extends AppCompatActivity {

    private LinearLayout row1, row2, row3, row4, row5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_journal);

        row1 = findViewById(id.linearlayout1);
        row2 = findViewById(id.linearlayout2);
        row3 = findViewById(id.linearlayout3);
        row4 = findViewById(id.linearlayout4);
        row5 = findViewById(id.linearlayout5);

        Calendar calNow = Calendar.getInstance();
        populateCalendar(calNow);

        // Onclick listeners for calendar navigation between months
        ImageButton left = findViewById(id.left);
        left.setOnClickListener(view -> {
            calNow.add(Calendar.MONTH, -1);
            depopulateCalendar();
            populateCalendar(calNow);
        });
        ImageButton right = findViewById(id.right);
        right.setOnClickListener(view -> {
            calNow.add(Calendar.MONTH, 1);
            depopulateCalendar();
            populateCalendar(calNow);
        });

        // Navigation bar code
        BottomNavigationView nav = findViewById(id.bottomNavigationView);
        nav.getMenu().findItem(id.journal).setChecked(true);
        nav.setOnItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == id.activity_main) {
                intent = new Intent(JournalActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (itemId == id.sleep) {
                intent = new Intent(JournalActivity.this, SleepActivity.class);
                startActivity(intent);
            } else if (itemId == id.guide) {
                intent = new Intent(JournalActivity.this, GuideActivity.class);
                startActivity(intent);
            } else {
                return false;
            }
            return true;
        });
    }

    private void depopulateCalendar() {
        row1.removeAllViews();
        row2.removeAllViews();
        row3.removeAllViews();
        row4.removeAllViews();
        row5.removeAllViews();
    }

    private void populateCalendar(Calendar calNew) {

        // Set month title to today's month
        TextView monthTextView = findViewById(id.month);
        String monthText = calNew.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " +  calNew.get(Calendar.YEAR);
        monthTextView.setText(monthText);
        int month = calNew.get(Calendar.MONTH) + 1;

        ArrayList<JournalModel> journals = new ArrayList<>();

        Cursor cursor = getContentResolver().query(
                DatabaseContract.Journal_Table.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                // Create alarm object for each row in cursor
                @SuppressLint("Range") int journalID = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Journal_Table._ID));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseContract.Journal_Table.COLUMN_DATE));
                @SuppressLint("Range") String journal = cursor.getString(cursor.getColumnIndex(DatabaseContract.Journal_Table.COLUMN_JOURNAL));

                // Add each journal model to arraylist
                JournalModel journalObject = new JournalModel(journalID, date, journal);
                journals.add(journalObject);
            } while (cursor.moveToNext());
        }
        cursor.close();


        // Populate grid view with buttons for each day of the month
        for (int i = 1; i <= calNew.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            // Dynamically creating buttons
            Button btnTag = new Button(this);
            btnTag.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            btnTag.setId(i);
            btnTag.setOnClickListener(view -> {
                int Id = view.getId();
                String date = Id + "-" + month + "-" + calNew.get(Calendar.YEAR);

                // Start new activity to enter journal details
                Intent intent = new Intent(JournalActivity.this, CreateJournalActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            });
            btnTag.setOnLongClickListener(view -> {
                String date = view.getId() + "-" + month + "-" + calNew.get(Calendar.YEAR);
                int result = getContentResolver().delete(DatabaseContract.Journal_Table.CONTENT_URI, DatabaseContract.Journal_Table.COLUMN_DATE + " = '" + date + "'", null);
                if (result == 1) {
                    view.setBackgroundResource(drawable.grey_button_bg);
                }
                return result == 1;
            });
            boolean flag = false;
            for (JournalModel el : journals ) {
                // If date has associated journal, colour button blue
                if ((i + "-" + month + "-" + calNew.get(Calendar.YEAR)).equals(el.getDate())) {
                    flag = true;
                }
            }
            if (flag) {
                btnTag.setBackgroundResource(drawable.blue_button_bg);
            } else {
                btnTag.setBackgroundResource(drawable.grey_button_bg);
            }


            // Space object
            Space space = new Space(this);
            space.setLayoutParams(new ViewGroup.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT));

            // Adding buttons to rows of max size 7, 1 row per week of the month
            if (i <= 7) {
                row1.addView(btnTag);
                if (i != 7) {
                    row1.addView(space);
                }
            } else if (i <= 14) {
                row2.addView(btnTag);
                if (i != 14) {
                    row2.addView(space);
                }
            } else if (i <= 21) {
                row3.addView(btnTag);
                if (i != 21) {
                    row3.addView(space);
                }
            } else if (i <= 28) {
                row4.addView(btnTag);
                if (i != 28) {
                    row4.addView(space);
                }
            } else {
                row5.addView(btnTag);
                if (i != calNew.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    row5.addView(space);
                }
            }
        }
    }
}