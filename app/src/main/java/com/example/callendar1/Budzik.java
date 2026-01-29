package com.example.callendar1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Budzik extends AppCompatActivity {

    private Button backButton;
    private Button Button2;
    private CalendarView kalendarzBudzik;
    private TextView dataBudzik;
    private EditText godzinaBudzik;
    private Button budzikButton;
    private ListView budzikList;

    private BudzikDataBaseHelper budzikdataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budzik);

        backButton = findViewById(R.id.Next2);
        Button2 = findViewById(R.id.button2);
        kalendarzBudzik = findViewById(R.id.calendarView2);
        dataBudzik = findViewById(R.id.textView2);
        godzinaBudzik = findViewById(R.id.editTextText);
        budzikButton = findViewById(R.id.button);
        budzikList = findViewById(R.id.budzikList);

        // FIX: create DB helper here (NOT as field initializer using Budzik.this)
        budzikdataBaseHelper = new BudzikDataBaseHelper(this);

        // Set initial date text to today's date (or calendar current date)
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dataBudzik.setText(df.format(new Date(kalendarzBudzik.getDate())));

        ShowWakeUpList();

        Intent intent = new Intent(Budzik.this,WakeUpService.class);
        startService(intent);

        backButton.setOnClickListener(v -> finish());

        kalendarzBudzik.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // month is 0-based
            String selectedDate = String.format(Locale.getDefault(),
                    "%04d-%02d-%02d", year, (month + 1), dayOfMonth);
            dataBudzik.setText(selectedDate);
        });

        budzikButton.setOnClickListener(v -> {
            String time = godzinaBudzik.getText().toString().trim();
            String date = dataBudzik.getText().toString().trim();

            if (date.isEmpty()) {
                Toast.makeText(this, "Choose a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (time.isEmpty()) {
                Toast.makeText(this, "Enter time (e.g. 10:30)", Toast.LENGTH_SHORT).show();
                return;
            }

            WakeUpModel wakeupModel = new WakeUpModel(
                    0,
                    time,
                    date,
                    false
            );

            boolean success = budzikdataBaseHelper.addOne(wakeupModel);
            Toast.makeText(this, "Saved = " + success, Toast.LENGTH_SHORT).show();

            ShowWakeUpList();
        });

        Button2.setOnClickListener(v -> {
            Intent intent1 = new Intent(Budzik.this, Pomodoro.class);
            startActivity(intent1);
        });

        budzikList.setOnItemLongClickListener((parent, view, position, id) -> {
            WakeUpModel clicked = (WakeUpModel) parent.getItemAtPosition(position);

            new AlertDialog.Builder(Budzik.this)
                    .setTitle("Usuwanie zadania")
                    .setMessage("Czy na pewno chcesz usunąć?\n\n" +
                            clicked.getkiedywakeup() + " " + clicked.getGodzina())
                    .setPositiveButton("Usuń", (dialog, which) -> {
                        boolean ok = budzikdataBaseHelper.deleteOne(clicked);
                        Toast.makeText(this, "Deleted = " + ok, Toast.LENGTH_SHORT).show();
                        ShowWakeUpList();
                    })
                    .setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss())
                    .show();

            return true;
        });

    }

    private void ShowWakeUpList() {
        WakeUpAdapter adapter = new WakeUpAdapter(
                this,
                budzikdataBaseHelper.getEveryOne(),
                budzikdataBaseHelper
        );
        budzikList.setAdapter(adapter);
    }
}
