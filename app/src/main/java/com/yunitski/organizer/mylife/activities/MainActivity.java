package com.yunitski.organizer.mylife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.adapters.SectionsPagerAdapter;
import com.yunitski.organizer.mylife.dbhelper.DbHelper;
import com.yunitski.organizer.mylife.dbhelper.InputData;
import com.yunitski.organizer.mylife.fragments.FragmentMorning;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fabMorning, fabDay, fabEvening;
    String time, morningTaskText, morningTaskTime;
    DbHelper dbHelper;
    private FragmentRefreshListener fragmentRefreshListener;
    private FragmentRefreshListener1 fragmentRefreshListener1;
    private FragmentRefreshListener2 fragmentRefreshListener2;

    TextView date;

    public static final String DATE_FILE = "dateFile";
    public static final String DATE_KEY = "dateKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        fabMorning = findViewById(R.id.fabMorning);
        fabDay = findViewById(R.id.fabDay);
        fabEvening = findViewById(R.id.fabEvening);
        time = timeCurrent();
        date = findViewById(R.id.date);
        date.setText(dateCurrent());
        SharedPreferences sharedPreferences = getSharedPreferences(DATE_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DATE_KEY, date.getText().toString());
        editor.apply();
        date.setOnClickListener(this);
        fabMorning.show();
        fabDay.hide();
        fabEvening.hide();
        fabMorning.setOnClickListener(this);
        fabDay.setOnClickListener(this);
        fabEvening.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        dbHelper = new DbHelper(this);
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                fabDay.hide();
                fabEvening.hide();
                fabMorning.show();
                if(getFragmentRefreshListener()!= null){
                    getFragmentRefreshListener().onRefresh();
                }
                break;
            case 1:
                fabMorning.hide();
                fabEvening.hide();
                fabDay.show();

                if (getFragmentRefreshListener1() != null){
                    getFragmentRefreshListener1().onRefresh();
                }
                break;

            case 2:
                fabMorning.hide();
                fabDay.hide();
                fabEvening.show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabMorning){
            launchDialog(InputData.TaskEntry.COLUMN_MORNING_TASK, InputData.TaskEntry.COLUMN_MORNING_TASK_TIME, InputData.TaskEntry.COLUMN_MORNING_TASK_STATUS, InputData.TaskEntry.COLUMN_MORNING_TASK_DATE, InputData.TaskEntry.MORNING_TABLE);

        } else if (v.getId() == R.id.fabDay){
            launchDialog(InputData.TaskEntry.COLUMN_DAY_TASK, InputData.TaskEntry.COLUMN_DAY_TASK_TIME, InputData.TaskEntry.COLUMN_DAY_TASK_STATUS, InputData.TaskEntry.COLUMN_DAY_TASK_DATE, InputData.TaskEntry.DAY_TABLE);

        } else if (v.getId() == R.id.fabEvening){
            launchDialog(InputData.TaskEntry.COLUMN_EVENING_TASK, InputData.TaskEntry.COLUMN_EVENING_TASK_TIME, InputData.TaskEntry.COLUMN_EVENING_TASK_STATUS, InputData.TaskEntry.COLUMN_EVENING_TASK_DATE, InputData.TaskEntry.EVENING_TABLE);
        } else if (v.getId() == R.id.date){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.date_picker, null);
            builder.setView(view)
                    .setTitle("Date")
                    .setCancelable(false);
            TextView chDate = view.findViewById(R.id.dateTextView);
            chDate.setText(date.getText().toString());
            CalendarView datePicker = view.findViewById(R.id.datePicker);
            String[] hh = chDate.getText().toString().split("\\.");
            int ddd = Integer.parseInt(hh[0]);
            int mmm = Integer.parseInt(hh[1]);
            int yyy = Integer.parseInt(hh[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, yyy);
            calendar.set(Calendar.MONTH, mmm - 1);
            calendar.set(Calendar.DAY_OF_MONTH, ddd);
            long milliTime = calendar.getTimeInMillis();
            datePicker.setDate(milliTime, true, true);
            datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    String mon = "" + (month + 1);
                    if (month < 10){
                        mon = "0" + (month + 1);
                    }
                    String day = "" + dayOfMonth;
                    if (dayOfMonth < 10){
                        day = "0" + dayOfMonth;
                    }
                    String dd = "" + day + "." + mon + "." + year;
                    chDate.setText(dd);
                }
            });
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    date.setText(chDate.getText().toString());
//                    SharedPreferences sharedPreferences = getSharedPreferences(DATE_FILE, MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(DATE_KEY, date.getText().toString());
//                    editor.apply();

                    if(getFragmentRefreshListener()!= null){
                        getFragmentRefreshListener().onRefresh();
                    }

                    if(getFragmentRefreshListener1()!= null){
                        getFragmentRefreshListener1().onRefresh();
                    }

                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    }

    private void launchDialog(String taskTextColumn, String taskTimeColumn, String taskStatusColumn, String taskDateColumn, String table){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.task_picker_dialog, null);
        EditText taskEditText = view.findViewById(R.id.taskEditText);
        TimePicker taskTimePicker = view.findViewById(R.id.taskTimePicker);
        taskTimePicker.setIs24HourView(true);
        taskTimePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            String m = "" + minute;
            if (minute < 10){
                m = "0" + minute;
            }
            time = "" + hourOfDay + ":" + m;
        });
        builder.setTitle("Задача")
                .setCancelable(false)
                .setView(view)
                .setPositiveButton("ok", (dialog, which) -> {
                    morningTaskText = taskEditText.getText().toString();
                    morningTaskTime = time;
                    if (!taskEditText.getText().toString().isEmpty()){
                        ContentValues values = new ContentValues();
                        values.put(taskTextColumn, taskEditText.getText().toString());
                        values.put(taskTimeColumn, time);
                        values.put(taskStatusColumn, "wait");
                        values.put(taskDateColumn, date.getText().toString());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.insert(table, null, values);
                        db.close();
                    }
                    if (getFragmentRefreshListener() != null){
                        getFragmentRefreshListener().onRefresh();
                    }
                    if (getFragmentRefreshListener1() != null){
                        getFragmentRefreshListener1().onRefresh();
                    }

                }).setNegativeButton("cancel", (dialog, which) -> {

                });
        builder.create().show();
    }

    private String timeCurrent(){
        Calendar calendar = new GregorianCalendar();
        int m = calendar.get(Calendar.MINUTE);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        String mm = "" + m;
        if (m < 10){
            mm = "0" + m;
        }
        return "" + h + ":" + mm;
    }

    private String dateCurrent(){
        Calendar calendar = new GregorianCalendar();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        String dd = "" + d;
        String mm = "" + (m + 1);
        if (d < 10){
            dd = "0" + d;
        }
        if (m < 10){
            mm = "0" + (m + 1);
        }
        return "" + dd + "." + mm + "." + y;
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }

    public FragmentRefreshListener1 getFragmentRefreshListener1() {
        return fragmentRefreshListener1;
    }

    public void setFragmentRefreshListener1(FragmentRefreshListener1 fragmentRefreshListener1) {
        this.fragmentRefreshListener1 = fragmentRefreshListener1;
    }

    public interface FragmentRefreshListener1{
        void onRefresh();
    }

    public FragmentRefreshListener2 getFragmentRefreshListener2() {
        return fragmentRefreshListener2;
    }

    public void setFragmentRefreshListener2(FragmentRefreshListener2 fragmentRefreshListener2) {
        this.fragmentRefreshListener2 = fragmentRefreshListener2;
    }

    public interface FragmentRefreshListener2{
        void onRefresh();
    }
    public String getMyData() {
        return date.getText().toString();
    }
}