package com.example.callendar1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class WakeUpService extends Service {

    private static final String CHANNEL_ID = "wakeup_channel";
    private static final int NOTIF_ID = 1001;

    private Thread backgroundThread;
    private volatile boolean isRunning = false;

    private BudzikDataBaseHelper budzikDataBaseHelper;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private MediaPlayer player;

    // Prevent multiple triggers in the same minute (alarmId + yyyy-MM-dd + HH:mm)
    private final Set<String> firedKeys = new HashSet<>();




    @Override
    public void onCreate() {
        super.onCreate();
        budzikDataBaseHelper = new BudzikDataBaseHelper(getApplicationContext());
        createChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            backgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    alarmLoop();
                    stopSelf();
                }
            });
            backgroundThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;

        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
        }

        stopRinging();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void alarmLoop() {
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());

        while (isRunning && !Thread.currentThread().isInterrupted()) {

            Date now = new Date();
            String nowDate = dateFmt.format(now);
            String nowTime = timeFmt.format(now); // "HH:mm"

            List<WakeUpModel> result = budzikDataBaseHelper.getEveryOne();

            for (WakeUpModel element : result) {
                if (element == null) continue;

                // If your model has "enabled", use it. If not, remove this block.
                try {
                    if (!element.isEnabled()) continue;
                } catch (Throwable ignored) {
                    // method not present - ignore
                }

                if (element.getkiedywakeup() == null || element.getGodzina() == null) continue;

                // Date must match exactly
                if (!nowDate.equals(element.getkiedywakeup().trim())) continue;

                // Normalize time to "HH:mm"
                String alarmTime = normalizeToHHmm(element.getGodzina());
                if (alarmTime == null) continue;

                if (nowTime.equals(alarmTime) && element.isEnabled()) {
                    String key = element.getId() + "#" + nowDate + "#" + nowTime;
                    if (!firedKeys.contains(key)) {
                        firedKeys.add(key);
                        ringChurchBellOneMinute();
                        //isRunning = false;
                    }
                }
            }

            // Clear old keys when minute changes a lot over time (simple cap)
            if (firedKeys.size() > 200) firedKeys.clear();

            // Don’t busy-spin
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String normalizeToHHmm(String input) {
        if (input == null) return null;
        input = input.trim();
        if (input.isEmpty()) return null;

        // Accepts "7", "7:5", "07:05", "7:05"
        // We'll parse with H:mm when ":" exists, else treat as hour only.
        try {
            if (input.contains(":")) {
                Date t = new SimpleDateFormat("H:mm", Locale.getDefault()).parse(input);
                if (t == null) return null;
                return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(t);
            } else {
                int hour = Integer.parseInt(input);
                if (hour < 0 || hour > 23) return null;
                return String.format(Locale.getDefault(), "%02d:00", hour);
            }
        } catch (ParseException | NumberFormatException e) {
            return null;
        }
    }

    private void ringChurchBellOneMinute() {
        mainHandler.post(() -> {
            // already ringing?
            if (player != null && player.isPlaying()) return;

            stopRinging();

            player = MediaPlayer.create(this, R.raw.church_bell);
            if (player != null) {
                player.setLooping(true);
                player.start();
            }

            // auto stop after 60 seconds
            mainHandler.postDelayed(this::stopRinging, 60_000);
        });
    }

    private void stopRinging() {
        mainHandler.removeCallbacksAndMessages(null);

        if (player != null) {
            try {
                if (player.isPlaying()) player.stop();
            } catch (Exception ignored) {}
            try {
                player.release();
            } catch (Exception ignored) {}
            player = null;
        }
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Alarm running")
                .setContentText("Waiting for alarm time…")
                .setOngoing(true)
                .build();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID,
                    "WakeUp Alarm",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(ch);
        }
    }
}
