package com.example.callendar1;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.callendar1.BudzikDataBaseHelper;
import com.example.callendar1.R;
import com.example.callendar1.WakeUpModel;

import java.text.SimpleDateFormat;
import java.time.chrono.ChronoLocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class WakeUpService extends Service {


    //private CountDownTimer countDownTimer;
    Thread backgroundThread;
    BudzikDataBaseHelper budzikDataBaseHelper = new BudzikDataBaseHelper(WakeUpService.this);
    ;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;


            backgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Alarm(budzikDataBaseHelper);
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
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void Alarm2(BudzikDataBaseHelper budzikDataBaseHelper) {
        List<WakeUpModel> result;
        result = budzikDataBaseHelper.getEveryOne();
        for (WakeUpModel element : result) {

            java.sql.Date kiedywakeup_date = java.sql.Date.valueOf(element.getkiedywakeup());

            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate_string = formatter.format(currentDate);
            java.sql.Date currentdate_date = java.sql.Date.valueOf(currentDate_string);
            long ilemilisekund = (kiedywakeup_date.getTime() - currentdate_date.getTime());
            long ileminut = ilemilisekund / (60 * 1000);
            if (ileminut >= -28800 && ileminut <= 28800) {
                final MediaPlayer churchbellsoundMP = MediaPlayer.create(this, R.raw.church_bell);
                churchbellsoundMP.start();
            }
        }
    }

    private void Alarm(BudzikDataBaseHelper budzikDataBaseHelper) {
        final String TAG = "WakeUpService";
        Log.e(TAG, " is working");
        long alarm_Godzina_godzina = 0;
        long alarm_Godzina_minuta = 0;
        do {
            List<WakeUpModel> result = budzikDataBaseHelper.getEveryOne();
            for (WakeUpModel element : result) {
                Log.e(TAG, " is working " + String.valueOf(element.getId()));
                Date currentDate = new Date();
                long currentData_Godzina = Long.parseLong(currentDate.toString().split(" ")[3].split(":")[0]);
                long currentData_Minuta = Long.parseLong(currentDate.toString().split(" ")[3].split(":")[1]);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate_string = formatter.format(currentDate);
                java.sql.Date currentdate_date = java.sql.Date.valueOf(currentDate_string);
                java.sql.Date alarm_date = java.sql.Date.valueOf(element.getkiedywakeup());


                if (element.getGodzina().contains(":")) {
                    var test = element.getGodzina().split(":")[0];
                    alarm_Godzina_godzina = Long.parseLong(element.getGodzina().split(":")[0]);
                    alarm_Godzina_minuta = Long.parseLong(element.getGodzina().split(":")[1]);
                } else {
                    alarm_Godzina_godzina = Long.parseLong(element.getGodzina());
                    alarm_Godzina_minuta = 0;
                }


                long alarm_day = alarm_date.getTime() / (10000 * 60 * 60);
                long curr_day = currentdate_date.getTime() / (10000 * 60 * 60);
                long roznicaczasu = Math.abs(alarm_day - curr_day);
                //long iledni = ilemilisekund / (60 * 60 * 1000);
                if (roznicaczasu == 0 && currentData_Godzina == alarm_Godzina_godzina && currentData_Minuta == alarm_Godzina_minuta) {
                    isRunning = false;
                    final MediaPlayer myalert = MediaPlayer.create(this, R.raw.church_bell);
                    myalert.start();
                }
            }
        } while (isRunning);
    }
}
