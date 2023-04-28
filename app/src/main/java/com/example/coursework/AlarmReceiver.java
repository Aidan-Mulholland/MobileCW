package com.example.coursework;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    public static MediaPlayer player;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        player = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        player.start();

        Intent i = new Intent(context, AlarmScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ALARM")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Alarm")
                .setContentText("Wake up")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (notificationManagerCompat.areNotificationsEnabled()) {
            notificationManagerCompat.notify(123, builder.build());
        }

        context.startActivity(i);
    }
}
