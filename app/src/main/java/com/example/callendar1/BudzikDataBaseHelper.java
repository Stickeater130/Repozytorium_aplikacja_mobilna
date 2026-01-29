package com.example.callendar1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BudzikDataBaseHelper extends SQLiteOpenHelper {

    public static final String WAKE_UP_TABLE = "WAKE_UP_TABLE";
    public static final String COLUMN_GODZINA = "GODZINA";
    public static final String COLUMN_KIEDY_WAKE_UP = "KIEDY_WAKE_UP";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_ENABLED = "ENABLED";

    // FIX: bump DB version to support enabled column upgrades safely
    private static final int DB_VERSION = 2;

    public BudzikDataBaseHelper(@Nullable Context context) {
        super(context, "db.Wakeup", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement =
                "CREATE TABLE " + WAKE_UP_TABLE + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_GODZINA + " TEXT, " +
                        COLUMN_KIEDY_WAKE_UP + " TEXT, " +
                        COLUMN_ENABLED + " INTEGER DEFAULT 0" +
                        ")";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Minimal safe upgrade: add ENABLED column if upgrading from older DB
        if (oldVersion < 2) {
            try {
                db.execSQL("ALTER TABLE " + WAKE_UP_TABLE +
                        " ADD COLUMN " + COLUMN_ENABLED + " INTEGER DEFAULT 0");
            } catch (Exception ignored) {
            }
        }
    }

    public boolean addOne(WakeUpModel wakeup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GODZINA, wakeup.getGodzina());
        cv.put(COLUMN_KIEDY_WAKE_UP, wakeup.getkiedywakeup());
        cv.put(COLUMN_ENABLED, wakeup.isEnabled() ? 1 : 0);

        long insert = db.insert(WAKE_UP_TABLE, null, cv);
        return insert != -1;
    }

    public boolean setEnabled(int id, boolean enabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ENABLED, enabled ? 1 : 0);

        int rows = db.update(WAKE_UP_TABLE, cv, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public List<WakeUpModel> getEveryOne() {
        List<WakeUpModel> resultList = new ArrayList<>();

        String queryString = "SELECT * FROM " + WAKE_UP_TABLE +
                " ORDER BY " + COLUMN_KIEDY_WAKE_UP + " ASC, " + COLUMN_GODZINA + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int taskid = cursor.getInt(0);
                    String task_godzina = cursor.getString(1);
                    String taskkiedy_wake_up = cursor.getString(2);

                    int enabledInt = cursor.getInt(3);
                    boolean enabled = (enabledInt == 1);

                    resultList.add(new WakeUpModel(taskid, task_godzina, taskkiedy_wake_up, enabled));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return resultList;
    }

    public boolean deleteOne_(WakeUpModel wakeupModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(WAKE_UP_TABLE, COLUMN_ID + "=?", new String[]{String.valueOf(wakeupModel.getId())});
        return rows > 0;
    }

    public boolean deleteOne(WakeUpModel wakeupModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(WAKE_UP_TABLE, COLUMN_ID + "=?",
                new String[]{String.valueOf(wakeupModel.getId())});
        return rows > 0;
    }

    public boolean deleteOneSimple(String wakeupModel) {
        int id = Integer.parseInt(wakeupModel.split(" ")[0]);
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(WAKE_UP_TABLE, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }


    // (Optional) if you still want a simple list of strings
    public List<String> getEveryOneSimple() {
        List<String> resultList = new ArrayList<>();

        String queryString = "SELECT * FROM " + WAKE_UP_TABLE +
                " ORDER BY " + COLUMN_KIEDY_WAKE_UP + " ASC, " + COLUMN_GODZINA + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    int taskid = cursor.getInt(0);
                    String task_godzina = cursor.getString(1);
                    String taskkiedy_wake_up = cursor.getString(2);

                    resultList.add(taskid + " " + taskkiedy_wake_up + " " + task_godzina);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return resultList;
    }

    public boolean deleteOneSimple_(String wakeupModel) {
        Integer id = Integer.parseInt(wakeupModel.split(" ")[0]);
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(WAKE_UP_TABLE, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }
}
