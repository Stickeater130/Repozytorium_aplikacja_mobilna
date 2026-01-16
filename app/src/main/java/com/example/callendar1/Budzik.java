package com.example.callendar1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Budzik extends AppCompatActivity {
    Button backButton;
    CalendarView kalendarzBudzik;
    TextView dataBudzik;
    EditText godzinaBudzik;
    Button budzikButton;
    ListView budzikList;
    BudzikDataBaseHelper budzikdataBaseHelper = new BudzikDataBaseHelper(Budzik.this);
    ArrayAdapter<WakeUpModel> adapterWakeUpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budzik);
        backButton = findViewById(R.id.Next2);

        kalendarzBudzik = findViewById(R.id.calendarView2);
        dataBudzik = findViewById(R.id.textView2);
        godzinaBudzik = findViewById(R.id.editTextText);
        budzikButton = findViewById(R.id.button);
        budzikList = findViewById(R.id.budzikList);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        kalendarzBudzik.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Miesiace w Kalendarzu zaczynają się od 0 (styczeń)
                String selectedDate = year + "-" + (month + 1) + "-" +dayOfMonth ;
                dataBudzik.setText(selectedDate);
            }
        });
        budzikButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WakeUpModel wakeupModel = null;

                try
                {
                    wakeupModel = new WakeUpModel( 1,godzinaBudzik.getText().toString(),dataBudzik.getText().toString());
                    Toast.makeText(Budzik.this,wakeupModel.toString(),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(Budzik.this,"Error",Toast.LENGTH_SHORT).show();
                }
                if (wakeupModel != null) {
                    boolean success = budzikdataBaseHelper.addOne(wakeupModel);
                    Toast.makeText(Budzik.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                }
                ShowWakeUpList(budzikdataBaseHelper);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void ShowWakeUpList(BudzikDataBaseHelper budzikDataBaseHelper)
    {
        adapterWakeUpModel = new ArrayAdapter<WakeUpModel>(Budzik.this, android.R.layout.simple_list_item_1, budzikDataBaseHelper.getEveryOne());
        budzikList.setAdapter(adapterWakeUpModel);
    }
}



