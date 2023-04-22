package com.example.coursework;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;


public class AlarmScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_screen);

        // Start alarm sound
        MediaPlayer mediaPlayer = MediaPlayer.create(AlarmScreenActivity.this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();

        // Alarm sound loops until dismiss button clicked
        Button dismiss = findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop alarm
                mediaPlayer.stop();
                Intent alarmDone = new Intent(AlarmScreenActivity.this, SleepActivity.class);
                startActivity(alarmDone);
            }
        });
    }
}