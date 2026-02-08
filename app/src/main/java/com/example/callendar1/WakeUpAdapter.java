package com.example.callendar1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.List;

public class WakeUpAdapter extends ArrayAdapter<WakeUpModel> {

    private final BudzikDataBaseHelper db;

    public WakeUpAdapter(@NonNull Context context,
                         @NonNull List<WakeUpModel> items,
                         @NonNull BudzikDataBaseHelper db) {
        super(context, 0, items);
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.item_budzik, parent, false);
        }

        TextView tvAlarm = row.findViewById(R.id.tvAlarm);
        Switch swAlarm = row.findViewById(R.id.swAlarm);

        WakeUpModel model = getItem(position);
        if (model == null) return row;

        tvAlarm.setText(model.getkiedywakeup() + " " + model.getGodzina());

        // IMPORTANT: clear old listener (ListView recycles rows)
        swAlarm.setOnCheckedChangeListener(null);
        swAlarm.setChecked(model.isEnabled());

        swAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.setEnabled(isChecked);
            db.setEnabled(model.getId(), isChecked);
        });

        return row;
    }

}
