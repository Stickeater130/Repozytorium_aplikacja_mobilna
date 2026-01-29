package com.example.callendar1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.callendar1.R;

import java.util.Locale;

public class Pomodoro extends AppCompatActivity {

    private static final long WORK_DURATION_MS  = 25L * 60L * 1000L;
    private static final long BREAK_DURATION_MS = 5L * 60L * 1000L;

    private enum Phase { WORK, BREAK }

    private RelativeLayout rootLayout;
    private TextView sessionTextView;
    private TextView timerTextView;
    private Button startButton;

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;

    private Phase currentPhase = Phase.WORK;
    private long timeLeftMs = WORK_DURATION_MS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro);

        rootLayout = findViewById(R.id.root_layout);
        sessionTextView = findViewById(R.id.session_text_view);
        timerTextView = findViewById(R.id.timer_text_view);
        startButton = findViewById(R.id.start_button);

        updateUIForPhase();
        updateTimerText();

        startButton.setOnClickListener(v -> {
            if (!timerRunning) {
                startTimer();
            } else {
                pauseTimer();
            }
        });
    }

    private void startTimer() {
        // Safety: cancel any previous timer
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(timeLeftMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMs = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;

                // Switch phase automatically when a timer ends
                switchPhase();
                startTimer(); // auto-start next phase
            }
        }.start();

        timerRunning = true;
        startButton.setText("Pause");
    }

    private void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        timerRunning = false;
        startButton.setText("Start");
    }

    private void switchPhase() {
        if (currentPhase == Phase.WORK) {
            currentPhase = Phase.BREAK;
            timeLeftMs = BREAK_DURATION_MS;
        } else {
            currentPhase = Phase.WORK;
            timeLeftMs = WORK_DURATION_MS;
        }
        updateUIForPhase();
        updateTimerText();
    }

    private void updateUIForPhase() {
        if (currentPhase == Phase.WORK) {
            sessionTextView.setText("WORK");
            rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        } else {
            sessionTextView.setText("REST");
            rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            startButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        }
    }

    private void updateTimerText() {
        long totalSeconds = timeLeftMs / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
