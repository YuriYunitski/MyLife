package com.yunitski.organizer.mylife.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.adapters.SectionsPagerAdapter;
import com.yunitski.organizer.mylife.dbhelper.DbHelper;
import com.yunitski.organizer.mylife.dbhelper.InputData;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fabMorning, fabDay, fabEvening;
    String time, morningTaskText, morningTaskTime;
    DbHelper dbHelper;
    private FragmentRefreshListener fragmentRefreshListener;

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
                break;
            case 1:
                fabMorning.hide();
                fabEvening.hide();
                fabDay.show();
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
            launchDialog(InputData.TaskEntry.COLUMN_MORNING_TASK, InputData.TaskEntry.COLUMN_MORNING_TASK_TIME, InputData.TaskEntry.MORNING_TABLE);
        } else if (v.getId() == R.id.fabDay){
            launchDialog(InputData.TaskEntry.COLUMN_DAY_TASK, InputData.TaskEntry.COLUMN_DAY_TASK_TIME, InputData.TaskEntry.DAY_TABLE);
        } else if (v.getId() == R.id.fabEvening){
            launchDialog(InputData.TaskEntry.COLUMN_EVENING_TASK, InputData.TaskEntry.COLUMN_EVENING_TASK_TIME, InputData.TaskEntry.EVENING_TABLE);
        }
    }

    private void launchDialog(String taskTextColumn, String taskTimeColumn, String table){
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
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.insert(table, null, values);
                        db.close();
                    }
                    if(getFragmentRefreshListener()!= null){
                        getFragmentRefreshListener().onRefresh();
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

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }
}