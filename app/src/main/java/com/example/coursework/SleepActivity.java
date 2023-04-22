package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.Calendar;

public class SleepActivity extends AppCompatActivity {

    private ListView alarmsView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        alarmsView = findViewById(R.id.alarmsView);

        // Populate alarm list with saved alarms
        updateAlarmListView();

        // Add alarm opens a time picker
        ImageButton addAlarm = findViewById(R.id.addAlarm);
        addAlarm.setOnClickListener(view -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(SleepActivity.this, (timePicker, hourOfDay, minute) -> {
                Calendar calNow = Calendar.getInstance();
                Calendar calSet = (Calendar) calNow.clone();
                calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calSet.set(Calendar.MINUTE, minute);
                calSet.set(Calendar.SECOND, 0);
                calSet.set(Calendar.MILLISECOND, 0);
                if (calSet.compareTo(calNow) <= 0) {
                    calSet.add(Calendar.DATE, 1);
                }
                AlarmModel newAlarm;
                try {
                    String timeText = calSet.get(Calendar.HOUR_OF_DAY) + ":" + calSet.get(Calendar.MINUTE);
                    newAlarm = new AlarmModel(-1, timeText, calSet.getTimeInMillis(), true);
                }
                catch (Exception e) {
                    Toast.makeText(SleepActivity.this, "Error creating alarm", Toast.LENGTH_SHORT).show();
                    newAlarm = new AlarmModel(-1, "error", 0, false);
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("ALARM_TIME_TEXT", newAlarm.getTimeText());
                contentValues.put("ALARM_TIME", newAlarm.getTimeInt());
                contentValues.put("ALARM_ACTIVE", true);
                Uri returnUri = getContentResolver().insert(DatabaseContract.Alarm_Table.CONTENT_URI, contentValues);
                setAlarm(calSet, ContentUris.parseId(returnUri));
                updateAlarmListView();
            }, 0, 0, false);
            timePickerDialog.show();
        });

        // alarmsView item listeners
        alarmsView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlarmModel clickedAlarm = (AlarmModel) adapterView.getItemAtPosition(i);
            int result = getContentResolver().delete(DatabaseContract.Alarm_Table.CONTENT_URI, DatabaseContract.Alarm_Table._ID + " = " + clickedAlarm.getId(), null);
            if (result == 1) {
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), clickedAlarm.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
            updateAlarmListView();
            return result == 1;
        });
        alarmsView.setOnItemClickListener((adapterView, view, i, l) -> {
            // Get alarm and deactivate in database
            AlarmModel clickedAlarm = (AlarmModel) adapterView.getItemAtPosition(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put("ALARM_ACTIVE", !clickedAlarm.getActive());
            int result = getContentResolver().update(DatabaseContract.Alarm_Table.CONTENT_URI, contentValues,DatabaseContract.Alarm_Table._ID + " = " + clickedAlarm.getId(), null);
            // If successful db write then follow up with an alarm cancel intent
            if (result == 1) {
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), clickedAlarm.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
            // Update alarm list view to represent the change
            updateAlarmListView();
        });

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.getMenu().findItem(R.id.sleep).setChecked(true);
        nav.setOnItemSelectedListener(item -> {
            Intent intent;
            int itemId = item.getItemId();
            if (itemId == R.id.activity_main) {
                intent = new Intent(SleepActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.journal) {
                intent = new Intent(SleepActivity.this, JournalActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.guide) {
                intent = new Intent(SleepActivity.this, GuideActivity.class);
                startActivity(intent);
            } else {
                return false;
            }
            return true;
        });
    }

    private void setAlarm(Calendar targetCal, long id){
        Toast.makeText(SleepActivity.this, "Made alarm", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), (int) id, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void updateAlarmListView() {
        Cursor cursor = getContentResolver().query(
                DatabaseContract.Alarm_Table.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        ArrayAdapter<AlarmModel> alarmsAdapter = new ArrayAdapter<>(SleepActivity.this, android.R.layout.simple_list_item_1);
        if (cursor.moveToFirst()) {
            do {
                // Create alarm object for each row in cursor
                @SuppressLint("Range") int alarmID = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Alarm_Table._ID));
                @SuppressLint("Range") String timeText = cursor.getString(cursor.getColumnIndex(DatabaseContract.Alarm_Table.COLUMN_TIME_TEXT));
                @SuppressLint("Range") int timeInt = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Alarm_Table.COLUMN_TIME));
                @SuppressLint("Range") boolean active = cursor.getInt(cursor.getColumnIndex(DatabaseContract.Alarm_Table.COLUMN_ACTIVE)) == 1;

                // Add each alarm model to array adapter
                AlarmModel alarm = new AlarmModel(alarmID, timeText, timeInt, active);
                alarmsAdapter.add(alarm);
            } while (cursor.moveToNext());
        }
        alarmsView.setAdapter(alarmsAdapter);
        cursor.close();
    }
}