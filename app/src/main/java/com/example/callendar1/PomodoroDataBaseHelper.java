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


public class PomodoroDataBaseHelper extends SQLiteOpenHelper {

    public static final String POMODORO_TABLE = "POMODORO_TABLE";
    public static final String COLUMN_KIEDYPRACA = "KIEDYPRACA";
    public static final String COLUMN_KIEDYPRZERWA = "KIEDYPRZERWA";
    public static final String COLUMN_STATUSPRZERWA = "STATUSPRZERWA";
    public static final String COLUMN_STATUSPRACA = "STATUSPRACA";
    public static final String COLUMN_ID = "ID";

    public PomodoroDataBaseHelper(@Nullable Context context) {
        super(context,"db.Tasks", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + POMODORO_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KIEDYPRACA + " TEXT, " + COLUMN_KIEDYPRZERWA + " TEXT, " + COLUMN_STATUSPRACA + " TEXT, " + COLUMN_STATUSPRZERWA +" TEXT)";
        db.execSQL(createTableStatement);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    public boolean addOne(PomodoroModel task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_KIEDYPRZERWA,task.getkiedyprzerwa());
        cv.put(COLUMN_KIEDYPRACA,task.getkiedypraca());
        cv.put(COLUMN_STATUSPRZERWA,task.statusprzerwa());
        cv.put(COLUMN_STATUSPRACA,task.statuspraca());
        long insert = db.insert(POMODORO_TABLE,null,cv);
        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public List<PomodoroModel> getEveryOne()
    {
        List<PomodoroModel>  resultList = new ArrayList<>();
        String querystring = "SELECT * FROM "+POMODORO_TABLE+" ORDER BY " + COLUMN_KIEDYPRZERWA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querystring,null);
        if(cursor.moveToFirst())
        {
            do
            {
                String taskstatus = "4";
                int taskid = cursor.getInt(0);
                String kiedypraca = cursor.getString(1);
                String kiedyprzerwa = cursor.getString(2);
                String statuspraca = cursor.getString(3);
                String statusprzerwa = cursor.getString(4);


                //Date kiedykoniec_date = Date.valueOf(taskkiedykoniec);
                java.sql.Date kiedypraca_date = java.sql.Date.valueOf(kiedypraca);

                Date currentDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate_string = formatter.format(currentDate);
                java.sql.Date currentdate_date = java.sql.Date.valueOf(currentDate_string);
                long ilemilisekund = (kiedypraca_date.getTime()-currentdate_date.getTime());
                long iledni = ilemilisekund / (24 * 60 * 60 * 1000);
                if (iledni<0)
                { taskstatus = "0";}
                if (iledni>=0 && iledni<=1)
                { taskstatus = "1";}
                if (iledni>=2 && iledni<=3)
                { taskstatus = "2";}
                if (iledni>=4 && iledni<=6)
                { taskstatus = "3";}
                if (iledni>6)
                { taskstatus = "4";}

                PomodoroModel pomodoromodel = new PomodoroModel(taskid,kiedypraca,kiedyprzerwa,statuspraca,statusprzerwa);
                resultList.add(pomodoromodel);
            } while (cursor.moveToNext() );
        }
        else
        {
            cursor.close();
            db.close();
        }
        return resultList;
    }

    public boolean deleteOne(PomodoroModel pomodoroModel)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String querystring = "DELETE FROM "+POMODORO_TABLE +" WHERE "+COLUMN_ID + "=" +pomodoroModel.getId();
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

