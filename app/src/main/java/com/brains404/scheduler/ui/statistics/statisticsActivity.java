package com.brains404.scheduler.ui.statistics;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brains404.scheduler.Entities.Task;
import com.brains404.scheduler.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import java.util.Set;

public class statisticsActivity extends AppCompatActivity {
TextView  task_stat ;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Change Theme
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);

        if (useDarkTheme) {
            setTheme(R.style.DarkAppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        task_stat = findViewById(R.id.tv_task_stat);
        ArrayList<Task> tasksList = new ArrayList<>();
        SharedPreferences taskPrefs = getSharedPreferences("taskPrefs", Context.MODE_PRIVATE);

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        int i=0;

        Gson gson = new Gson();
        // Get all Keys to loop
        Set<String> keys = taskPrefs.getAll().keySet();
        // Case Session cache not empty

        // Back Home Button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        if (keys.size() > 0) {
            for (String key : keys) {
                String json = taskPrefs.getString(key, "");
                Task myTask = gson.fromJson(json, Task.class);
                // Select To-Do Tasks Status[0]
                if ((myTask.getStatus() == 1)) {
                    tasksList.add(myTask);
                }
                i++;
            }
        }
       TextView stat_title =findViewById(R.id.progress_title);
        stat_title.setText(String.format("%s (%d/%d)", getResources().getString(R.string.task_progress), tasksList.size(), i));
        int percentage;
         try {
             percentage =tasksList.size()*100/i;

         }catch (ArithmeticException e){
          percentage=0;
         }
        task_stat.setText(String.format("%s%%", "".concat(String.valueOf(percentage))));

        seekBar.setProgress(percentage);
    }

}
