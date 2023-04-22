package com.example.coursework;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        // Start Alarm Screen Activity
        Intent alarmIntent = new Intent(context, AlarmScreenActivity.class);
        alarmIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}
