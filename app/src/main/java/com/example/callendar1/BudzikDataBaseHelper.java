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

    public static final String TASKS_TABLE = "TASKS_TABLE";
    public static final String COLUMN_OPIS = "OPIS";
    public static final String COLUMN_KIEDYKONIEC = "KIEDYKOINIEC";
    public static final String COLUMN_STATUS = "STATUS";
    public static final String COLUMN_ID = "ID";

    public BudzikDataBaseHelper(@Nullable Context context) {
        super(context,"db.Tasks", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TASKS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_OPIS + " TEXT, " + COLUMN_KIEDYKONIEC + " DATE, " + COLUMN_STATUS + " TEXT)";
        db.execSQL(createTableStatement);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    public boolean addOne(TaskModel task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_OPIS,task.getOpis());
        cv.put(COLUMN_KIEDYKONIEC,task.getKiedykoniec());
        cv.put(COLUMN_STATUS,task.getStatus());
        long insert = db.insert(TASKS_TABLE,null,cv);
        if (insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public List<TaskModel> getEveryOne()
    {
        List<TaskModel>  resultList = new ArrayList<>();
        String querystring = "SELECT * FROM "+TASKS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querystring,null);
        if(cursor.moveToFirst())
        {
            do
            {
                String taskstatus = "4";
                int taskid = cursor.getInt(0);
                String taskopis = cursor.getString(1);
                String taskkiedykoniec = cursor.getString(2);

                //Date kiedykoniec_date = Date.valueOf(taskkiedykoniec);
                java.sql.Date kiedykoniec_date = java.sql.Date.valueOf(taskkiedykoniec);

                Date currentDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate_string = formatter.format(currentDate);
                java.sql.Date currentdate_date = java.sql.Date.valueOf(currentDate_string);
                long ilemilisekund = (kiedykoniec_date.getTime()-currentdate_date.getTime());
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

                TaskModel taskmodel = new TaskModel(taskid,taskopis,taskkiedykoniec,taskstatus);
                resultList.add(taskmodel);
            } while (cursor.moveToNext() );
        }
        else
        {
            cursor.close();
            db.close();
        }
        return resultList;
    }

    public boolean deleteOne(TaskModel taskModel)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String querystring = "DELETE FROM "+TASKS_TABLE +" WHERE "+COLUMN_ID + "=" +taskModel.getId();
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

