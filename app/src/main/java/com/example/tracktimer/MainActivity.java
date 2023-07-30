package com.example.tracktimer;

import static com.example.tracktimer.DatabaseUtilsKt.addNoteAndUpdateAdapter;
import static com.example.tracktimer.DatabaseUtilsKt.fetchNotesAndUpdateAdapter;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_INTERVAL = 1 * 60 * 1000; // 1 minutes
    private static final String PREF_NAME = "MyPrefs";
    private static final String ALARM_ENABLED_KEY = "notificationEnabled";
    private BroadcastReceiver notificationReceiver;
    private NoteDatabase noteDatabase;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteDatabase = NoteDatabase.getInstance(this);

        createNotificationChannel();

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(new ArrayList<NoteEntity>());
        recyclerView.setAdapter(noteAdapter);

        fetchNotesAndUpdateAdapter(this);

        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("MyApp", "onReceive: Notification");
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter("Notification_Received"));

        Button toggleButton = findViewById(R.id.toggleButton);
        if (getNotificationAlarmEnabled()) {
            toggleButton.setText(getString(R.string.toggle_button_timer_text, "true"));
        } else {
            toggleButton.setText(R.string.toggle_button_stopped_text);
        }

        toggleButton.setOnClickListener(v -> {
            if (getNotificationAlarmEnabled()) {
                stopNotificationAlarm();
                saveNotificationAlarmEnabled(false);
                toggleButton.setText(R.string.toggle_button_stopped_text);
            } else {
                saveNotificationAlarmEnabled(true);
                long startTime = System.currentTimeMillis();
                startNotificationAlarm(startTime);
                toggleButton.setText(getString(R.string.toggle_button_timer_text, "true"));
            }
        });

        Button submitButton = findViewById(R.id.submitButton);
        EditText activityEditText = findViewById(R.id.activityEditTextText);

        submitButton.setOnClickListener(v-> {
            String noteText = activityEditText.getText().toString();
            long timestamp = System.currentTimeMillis();
            addNoteAndUpdateAdapter(this, timestamp, noteText);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    public NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }

    public NoteAdapter getNoteAdapter() {
        return noteAdapter;
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationReceiver.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private String formatTime(long timeInMillies) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(timeInMillies);
        return dateFormat.format(date);
    }

    private String formatElapsedTime(long elapsedTimeInMillies) {
        long hours = elapsedTimeInMillies / (1000 * 60 * 60);
        long minutes = (elapsedTimeInMillies % (1000 * 60 * 60)) / (1000 * 60);
        long seconds =  (elapsedTimeInMillies % (1000 * 60)) / 1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    private void saveNotificationAlarmEnabled(boolean enable) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ALARM_ENABLED_KEY, enable);
        editor.apply();
    }

    private boolean getNotificationAlarmEnabled() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ALARM_ENABLED_KEY, false);
    }

    private void startNotificationAlarm(long startTime) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, NOTIFICATION_INTERVAL, pendingIntent);
    }
    private void stopNotificationAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

}
