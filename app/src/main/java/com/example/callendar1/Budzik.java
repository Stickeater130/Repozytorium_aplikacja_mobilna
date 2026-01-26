package com.example.callendar1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Budzik extends AppCompatActivity {
    Button backButton;
    Button Button2;
    CalendarView kalendarzBudzik;
    TextView dataBudzik;
    EditText godzinaBudzik;
    Button budzikButton;
    ListView budzikList;
    BudzikDataBaseHelper budzikdataBaseHelper = new BudzikDataBaseHelper(Budzik.this);
    ArrayAdapter<WakeUpModel> adapterWakeUpModel;
    ArrayAdapter<String> adapterWakeUpModelSimple;
    Switch WakeUpSwitch;

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
        WakeUpSwitch = findViewById(R.id.switch1);
        ShowWakeUpListSimple(budzikdataBaseHelper);
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
                ShowWakeUpListSimple(budzikdataBaseHelper);
                //Alarm();
                //Alarm2(budzikdataBaseHelper);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Budzik.this, Pomodoro.class);
                startActivity(intent);
            }
        });

        WakeUpSwitch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (WakeUpSwitch.isChecked())
                {
                    Intent intent = new Intent(Budzik.this,WakeUpService.class);
                    startService(intent);
                } else {
                    Intent intent = new Intent(Budzik.this,WakeUpService.class);
                    stopService(intent);
                }
            }

        });

        budzikList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedtask = (String) parent.getItemAtPosition(position);

                new AlertDialog.Builder(Budzik.this)
                        .setTitle("Usuwanie zadania")
                        .setMessage("Czy na pewno chcesz usunąć?\n\n" + clickedtask.toString())
                        .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                budzikdataBaseHelper.deleteOneSimple(clickedtask);
                                ShowWakeUpListSimple(budzikdataBaseHelper);
                                Toast.makeText(Budzik.this,
                                        "Usunięto: " + clickedtask.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                return true; // very important: confirms we handled the long click
            }
        });


        //final MediaPlayer bellsoundMP = MediaPlayer.create(this,R.raw.notification_bell_sound);
        //bellsoundMP.start();
    }
    private void ShowWakeUpList(BudzikDataBaseHelper budzikDataBaseHelper)
    {
        adapterWakeUpModel = new ArrayAdapter<WakeUpModel>(Budzik.this, android.R.layout.simple_list_item_1, budzikDataBaseHelper.getEveryOne());
        budzikList.setAdapter(adapterWakeUpModel);
    }

    private void ShowWakeUpListSimple(BudzikDataBaseHelper budzikDataBaseHelper)
    {
        adapterWakeUpModelSimple = new ArrayAdapter<String>(Budzik.this, android.R.layout.simple_list_item_1, budzikDataBaseHelper.getEveryOneSimple());
        budzikList.setAdapter(adapterWakeUpModelSimple);
    }

    private void Alarm()
    {
        final MediaPlayer bellsoundMP = MediaPlayer.create(this,R.raw.notification_bell_sound);
        bellsoundMP.start();
    }

    private void Alarm2(BudzikDataBaseHelper budzikDataBaseHelper)
    {
        List<WakeUpModel> result;
        result = budzikDataBaseHelper.getEveryOne();
        for(WakeUpModel element:result)
        {

            java.sql.Date kiedywakeup_date = java.sql.Date.valueOf(element.getkiedywakeup());

            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate_string = formatter.format(currentDate);
            java.sql.Date currentdate_date = java.sql.Date.valueOf(currentDate_string);
            long ilemilisekund = (kiedywakeup_date.getTime()-currentdate_date.getTime());
            long ileminut = ilemilisekund / (60 * 1000);
            if (ileminut>=-28800 && ileminut<=28800)
            {
                final MediaPlayer churchbellsoundMP = MediaPlayer.create(this,R.raw.church_bell);
                churchbellsoundMP.start();
            }
        }



    }
}



