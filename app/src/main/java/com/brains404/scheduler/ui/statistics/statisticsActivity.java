package com.brains404.scheduler.ui.statistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.brains404.scheduler.Entities.Task;
import com.brains404.scheduler.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class statisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Change Theme
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);

        if(useDarkTheme) {
            setTheme(R.style.DarkAppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        TextView task_stat=(TextView)findViewById(R.id.task_stat_tv);
        RecyclerView recyclerView = findViewById(R.id.rv_tasks);
        ArrayList<Task> tasksList=new ArrayList<>() ;
        SharedPreferences taskPrefs =getSharedPreferences("taskPrefs", Context.MODE_PRIVATE);

        SeekBar seekBar=findViewById(R.id.seekBar);

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
            Iterator<String> itr = keys.iterator();
            while(itr.hasNext()){
                String json = taskPrefs.getString(itr.next(), "");
                Task myTask = gson.fromJson(json, Task.class);
                // Select To-Do Tasks Status[0]
                if( (myTask.getStatus()==0)) {
                    tasksList.add(myTask);
                }
                i++;
            }}
        int percentage=tasksList.size()*100/i;
        task_stat.setText("".concat(String.valueOf(percentage))+"%");

        seekBar.setProgress(percentage);
    }
}
