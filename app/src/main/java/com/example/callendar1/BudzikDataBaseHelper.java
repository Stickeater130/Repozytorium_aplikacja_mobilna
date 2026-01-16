package com.example.callendar1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class BudzikDataBaseHelper extends SQLiteOpenHelper {

    public static final String WAKE_UP_TABLE = "WAKE_UP_TABLE";
    public static final String COLUMN_GODZINA = "GODZINA";
    public static final String COLUMN_KIEDY_WAKE_UP = "KIEDY_WAKE_UP";
    public static final String COLUMN_ID = "ID";

    public BudzikDataBaseHelper(@Nullable Context context) {
        super(context,"db.Wakeup", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + WAKE_UP_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_GODZINA + " TEXT, " + COLUMN_KIEDY_WAKE_UP + " TEXT)";
        db.execSQL(createTableStatement);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    public boolean addOne(WakeUpModel wakeup)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GODZINA,wakeup.getGodzina());
        cv.put(COLUMN_KIEDY_WAKE_UP,wakeup.getkiedywakeup());
        long insert = db.insert(WAKE_UP_TABLE,null,cv);
        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public List<WakeUpModel> getEveryOne()
    {
        List<WakeUpModel>  resultList = new ArrayList<>();
        String querystring = "SELECT * FROM "+ WAKE_UP_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querystring,null);
        if(cursor.moveToFirst())
        {
            do
            {

                int taskid = cursor.getInt(0);
                String task_godzina = cursor.getString(1);
                String taskkiedy_wake_up = cursor.getString(2);

                WakeUpModel wakeUpModel = new WakeUpModel(taskid,task_godzina,taskkiedy_wake_up);
                resultList.add(wakeUpModel);
            } while (cursor.moveToNext() );
        }
        else
        {
            cursor.close();
            db.close();
        }
        return resultList;
    }

    public boolean deleteOne(WakeUpModel wakeupModel)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String querystring = "DELETE FROM "+ WAKE_UP_TABLE +" WHERE "+COLUMN_ID + "=" +wakeupModel.getId();
        Cursor cursor = db.rawQuery(querystring,null);
        if (cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

