package com.example.callendar1;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;


import androidx.core.content.ContextCompat;

public class AlarmMan extends  BroadcastReceiver
{
    MediaPlayer mp;


    @Override
    public void onReceive(Context context, Intent intent) {
        mp = MediaPlayer.create(context.getApplicationContext(), R.raw.church_bell);
        if (mp != null) mp.start();
    }
}
