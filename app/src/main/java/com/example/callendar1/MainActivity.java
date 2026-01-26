package com.example.callendar1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button SendButton;
    Button Next;
    Button Button2;
    EditText Opis;
    EditText KiedyKoniec;
    CalendarView Kalendarz;
    ListView ViewListNew;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
    ArrayAdapter adapterTaskModel;
    ArrayAdapter adapterTaskModelSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SendButton = findViewById(R.id.SendButton);
        Button2 = findViewById(R.id.button2);
        Next = findViewById(R.id.Next);
        KiedyKoniec = findViewById(R.id.editTextText_kiedykoniec);
        Opis = findViewById(R.id.opis);
        Kalendarz = findViewById(R.id.calendarView);
        ViewListNew = findViewById(R.id.listView);
        ShowTaskaListSimple(dataBaseHelper);

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskModel taskModel = null;

                try
                {
                    taskModel = new TaskModel( 1,Opis.getText().toString(),KiedyKoniec.getText().toString(),"1");
                    Toast.makeText(MainActivity.this,taskModel.toString(),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                boolean success = dataBaseHelper.addOne(taskModel);
                Toast.makeText(MainActivity.this,"Success = "+success,Toast.LENGTH_SHORT).show();
                ShowTaskaListSimple(dataBaseHelper);

            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Budzik.class);
                startActivity(intent);
            }
        });



        Kalendarz.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Miesiace w Kalendarzu zaczynają się od 0 (styczeń)
                String selectedDate = year + "-" + (month + 1) + "-" +dayOfMonth ;
                KiedyKoniec.setText(selectedDate);
            }
        });

        ViewListNew.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedtask = (String) parent.getItemAtPosition(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Usuwanie zadania")
                        .setMessage("Czy na pewno chcesz usunąć?\n\n" + clickedtask.toString())
                        .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.deleteOneSimple(clickedtask);
                                ShowTaskaListSimple(dataBaseHelper);
                                Toast.makeText(MainActivity.this,
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

    }

    private void ShowTaskaList(DataBaseHelper dataBaseHelper)
    {

        adapterTaskModel = new ArrayAdapter<TaskModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryOne()){
        //
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        view.setLayoutParams(params);

        TextView textview = (TextView) view;
        textview.setTextSize(15);
        textview.setGravity(Gravity.CENTER);
        textview.setTextColor(Color.BLACK);

        int p = 0;

        for (TaskModel c : dataBaseHelper.getEveryOne()) {
            if (c.getStatus().equals("2") && (position == p)) {

                ((TextView) view).setTextColor(Color.MAGENTA);

            }
            if (c.getStatus().equals("3") && (position == p)) {

                ((TextView) view).setTextColor(Color.BLUE);

            }
            if (c.getStatus().equals("4") && (position == p)) {

                ((TextView) view).setTextColor(Color.GREEN);

            }
            if (c.getStatus().equals("1") && (position == p)) {

                ((TextView) view).setTextColor(Color.RED);

            }
            if (c.getStatus().equals("0") && (position == p)) {

                ((TextView) view).setTextColor(Color.BLACK);

            }
            p++;
        }

        return textview;


    }
    };
        //
        ViewListNew.setAdapter(adapterTaskModel);


    }

    private void ShowTaskaListSimple(DataBaseHelper dataBaseHelper)
    {

        adapterTaskModelSimple = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryOneSimple()){
            //
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                view.setLayoutParams(params);

                TextView textview = (TextView) view;
                textview.setTextSize(15);
                textview.setGravity(Gravity.CENTER);
                textview.setTextColor(Color.BLACK);

                int p = 0;

                for (TaskModel c : dataBaseHelper.getEveryOne()) {
                    if (c.getStatus().equals("2") && (position == p)) {

                        ((TextView) view).setTextColor(Color.MAGENTA);

                    }
                    if (c.getStatus().equals("3") && (position == p)) {

                        ((TextView) view).setTextColor(Color.BLUE);

                    }
                    if (c.getStatus().equals("4") && (position == p)) {

                        ((TextView) view).setTextColor(Color.GREEN);

                    }
                    if (c.getStatus().equals("1") && (position == p)) {

                        ((TextView) view).setTextColor(Color.RED);

                    }
                    if (c.getStatus().equals("0") && (position == p)) {

                        ((TextView) view).setTextColor(Color.BLACK);

                    }
                    p++;
                }

                return textview;


            }
        };
        //
        ViewListNew.setAdapter(adapterTaskModelSimple);


    }

}