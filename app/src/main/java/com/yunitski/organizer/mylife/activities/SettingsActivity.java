package com.yunitski.organizer.mylife.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.yunitski.organizer.mylife.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Switch aSwitch;
    SharedPreferences sharedPreferences;
    public static final String NOTIFICATION_ENABLE_FILE = "notificationEnableFile.txt";
    public static final String NOTIFICATION_ENABLE_KEY = "notificationEnableKey";
    private ImageButton backImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        aSwitch = findViewById(R.id.enableNotification);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(NOTIFICATION_ENABLE_FILE, Context.MODE_PRIVATE);
        boolean isEnabled = sharedPreferences.getBoolean(NOTIFICATION_ENABLE_KEY, true);
        aSwitch.setChecked(isEnabled);
        aSwitch.setOnClickListener(v -> {
            if (aSwitch.isChecked()){
                sharedPreferences = getSharedPreferences(NOTIFICATION_ENABLE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(NOTIFICATION_ENABLE_KEY, true);
                editor.apply();
                Toast.makeText(getApplicationContext(), "enabled", Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferences = getSharedPreferences(NOTIFICATION_ENABLE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(NOTIFICATION_ENABLE_KEY, false);
                editor.apply();
                Toast.makeText(getApplicationContext(), "disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backImageButton){
            SettingsActivity.this.finish();
        }
    }
}