package com.example.coursework;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class AlarmScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        // Alarm sound loops until dismiss button clicked
        Button dismiss = findViewById(R.id.dismiss);
        dismiss.setOnClickListener(view -> {
            // Stop alarm
            AlarmReceiver.player.stop();
            Intent alarmDone = new Intent(AlarmScreenActivity.this, SleepActivity.class);
            startActivity(alarmDone);
        });
    }
}