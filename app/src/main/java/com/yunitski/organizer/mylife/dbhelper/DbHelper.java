package com.yunitski.organizer.mylife.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, InputData.DB_NAME, null, InputData.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableMorning = "CREATE TABLE " +
                InputData.TaskEntry.MORNING_TABLE +
                " (" +
                InputData.TaskEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InputData.TaskEntry.COLUMN_MORNING_TASK +
                " TEXT, " +
                InputData.TaskEntry.COLUMN_MORNING_TASK_TIME +
                " TEXT, " +
                InputData.TaskEntry.COLUMN_MORNING_TASK_STATUS +
                " TEXT);";
        db.execSQL(createTableMorning);

        String createTableDay = "CREATE TABLE " +
                InputData.TaskEntry.DAY_TABLE +
                " (" +
                InputData.TaskEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InputData.TaskEntry.COLUMN_DAY_TASK +
                " TEXT, " +
                InputData.TaskEntry.COLUMN_DAY_TASK_TIME +
                " TEXT, " +
                InputData.TaskEntry.COLUMN_DAY_TASK_STATUS +
                " TEXT);";
        db.execSQL(createTableDay);

        String createTableEvening = "CREATE TABLE " +
                InputData.TaskEntry.EVENING_TABLE +
                " (" +
                InputData.TaskEntry.COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InputData.TaskEntry.COLUMN_EVENING_TASK +
                " TEXT, " +
                InputData.TaskEntry.COLUMN_EVENING_TASK_TIME +
                " TEXT, " +
                InputData.TaskEntry.COLUMN_EVENING_TASK_STATUS +
                " TEXT);";
        db.execSQL(createTableEvening);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InputData.TaskEntry.MORNING_TABLE);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + InputData.TaskEntry.DAY_TABLE);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + InputData.TaskEntry.EVENING_TABLE);
        onCreate(db);
    }
}
