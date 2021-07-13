package com.yunitski.organizer.mylife.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.yunitski.organizer.mylife.R;
import com.yunitski.organizer.mylife.adapters.SectionsPagerAdapter;
import com.yunitski.organizer.mylife.dbhelper.DbHelper;
import com.yunitski.organizer.mylife.dbhelper.InputData;
import com.yunitski.organizer.mylife.receiver.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabMorning, fabDay, fabEvening;
    private String time;
    private DbHelper dbHelper;
    private FragmentRefreshListener fragmentRefreshListener;
    private FragmentRefreshListener1 fragmentRefreshListener1;
    private FragmentRefreshListener2 fragmentRefreshListener2;

    private TextView date;

    private ImageButton popupMenuImageButton;


    public static final String NOTIFICATION_CHANNEL_ID = "10001888" ;
    private final static String default_notification_channel_id = "default" ;

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
        popupMenuImageButton = findViewById(R.id.popupMenuImageButton);
        popupMenuImageButton.setOnClickListener(this);
        time = timeCurrent();
        date = findViewById(R.id.date);
        date.setText(dateCurrent());
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
        String[] tt = timeCurrent().split(":");
        if (Integer.parseInt(tt[0]) > 9 && Integer.parseInt(tt[0]) <= 19){
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            assert tab != null;
            tab.select();
        } else if (Integer.parseInt(tt[0]) > 19 && Integer.parseInt(tt[0]) <= 23){
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            assert tab != null;
            tab.select();
        } else {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            assert tab != null;
            tab.select();
        }
        dbHelper = new DbHelper(this);
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "myLifeReminderChannel";
            String description = "Channel for alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("myLifeChannelId", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                if (getFragmentRefreshListener2() != null){
                    getFragmentRefreshListener2().onRefresh();
                }
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
            datePicker.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
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
            });
            builder.setPositiveButton("ok", (dialog, which) -> {
                date.setText(chDate.getText().toString());
                if(getFragmentRefreshListener()!= null){
                    getFragmentRefreshListener().onRefresh();
                }
                if(getFragmentRefreshListener1()!= null){
                    getFragmentRefreshListener1().onRefresh();
                }
                if (getFragmentRefreshListener2() != null){
                    getFragmentRefreshListener2().onRefresh();
                }
            }).setNegativeButton("cancel", (dialog, which) -> {

            });
            builder.create().show();
        } else if (v.getId() == R.id.popupMenuImageButton){
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, popupMenuImageButton);
            popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.settingsMenu){
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                }
                return true;
            });
            popupMenu.show();
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
                    if (getFragmentRefreshListener2() != null){
                        getFragmentRefreshListener2().onRefresh();
                    }
//                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                    Intent intent = new Intent(this, AlarmReceiver.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTimeInMills(date.getText().toString(), time), pendingIntent);
//                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, getTimeInMills(date.getText().toString(), time), AlarmManager.INTERVAL_DAY, pendingIntent);
//                    Toast.makeText(getApplicationContext(), "" +(getTimeInMills(date.getText().toString(), time) - getTimeInMills(dateCurrent(), timeCurrent())), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent2 = PendingIntent.getActivity(getApplicationContext(), (int)(Math.random() * 1000000), i, 0);
                    scheduleNotification(getNotification(taskEditText.getText().toString(), pendingIntent2), (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), getTimeInMills(date.getText().toString(), time));
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

    private long getTimeInMills(String date, String time){
        long timeInMillis = 0;
        String fullDate = date + " " + time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date mDate = simpleDateFormat.parse(fullDate);
            timeInMillis = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMillis;
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

    private void scheduleNotification (Notification notification , int notificationId, long setTime) {
        Intent notificationIntent = new Intent( this, AlarmReceiver.class ) ;
        notificationIntent.putExtra("NOTIFICATION_ID" , notificationId ) ;
        notificationIntent.putExtra("NOTIFICATION" , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, (int)(Math.random() * 1000000), notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP , setTime , pendingIntent);
    }
    private Notification getNotification (String content, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "It's time!" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        builder.setContentIntent(pendingIntent);
        return builder.build() ;
    }

}