package com.example.callendar1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Pomodoro extends AppCompatActivity {

    private Button pomodoroNext;
    private TextView czypracuje;
    private View my;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isGreen = false;

    private final Runnable switchRunnable = new Runnable() {
        @Override
        public void run() {
            // Toggle color state
            isGreen = !isGreen;

            if (isGreen) {
                my.setBackgroundColor(ContextCompat.getColor(Pomodoro.this, R.color.green));
                czypracuje.setVisibility(View.VISIBLE);
            } else {
                my.setBackgroundColor(ContextCompat.getColor(Pomodoro.this, R.color.red));
                czypracuje.setVisibility(View.GONE);
            }

            // Repeat every 10 seconds
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pomodoro);

        // Connect views
        my = findViewById(R.id.pomodoro);
        pomodoroNext = findViewById(R.id.pomodoroNext);
        czypracuje = findViewById(R.id.textView3);

        // Start as RED (optional)
        my.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        czypracuje.setVisibility(View.GONE);

        // Button closes activity
        pomodoroNext.setOnClickListener(v -> finish());

        // Start switching every 10 seconds
        handler.postDelayed(switchRunnable, 1000);

        // If you want it to switch immediately at start instead, use:
        // handler.post(switchRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(switchRunnable); // prevent memory leaks
    }
}
