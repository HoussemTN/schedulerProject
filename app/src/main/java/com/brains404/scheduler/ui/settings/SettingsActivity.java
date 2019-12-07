package com.brains404.scheduler.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.brains404.scheduler.R;



public class SettingsActivity extends AppCompatActivity {
     Boolean isDarkTheme =false;
    SharedPreferences timeTablePrefs;

    SharedPreferences taskPrefs;
    Boolean isTaskClearClicked=false;
    Boolean isTimeTableClearClicked=false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);

        if(useDarkTheme) {
            setTheme(R.style.DarkAppTheme);
            isDarkTheme=true ;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Switch darkModeSwitch = findViewById(R.id.darkModeSwitch);
        Button btnClearTask = findViewById(R.id.btn_clear_tasks_data);
        Button btnClearTimeTable = findViewById(R.id.btn_clear_timetable_data);

        timeTablePrefs = getSharedPreferences("timeTablePrefs", Context.MODE_PRIVATE);
        taskPrefs = getSharedPreferences("taskPrefs", MODE_PRIVATE);
        //init Dark Mode Switch (Activated/Deactivated)
        darkModeSwitch.setChecked(isDarkTheme);
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);

            }
        });

        // simple Click + long Click to confirm Data Deletion
        btnClearTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_confirm_clear_data),Toast.LENGTH_SHORT).show();
                isTaskClearClicked=true;

            }
        });
        btnClearTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // TODO clear Tasks Prefs and show Toast Message
                if (isTaskClearClicked) {
                    taskPrefs.edit().clear().apply();
                     Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_tasks_cleared),Toast.LENGTH_SHORT).show();
                    isTaskClearClicked = false;
                }
                return true;
            }
        });

        // Clear TimeTable Data Button Events
        btnClearTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_confirm_clear_data),Toast.LENGTH_SHORT).show();
                isTimeTableClearClicked=true;
            }
        });
        btnClearTimeTable.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isTimeTableClearClicked)
                timeTablePrefs.edit().clear().apply();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_timetable_cleared),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //TODO Activate/Deactivate notification switch
      //Back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }


    }
    private void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences("PrefsTheme", MODE_PRIVATE).edit();
        editor.putBoolean("darkTheme", darkTheme);
        editor.apply();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}