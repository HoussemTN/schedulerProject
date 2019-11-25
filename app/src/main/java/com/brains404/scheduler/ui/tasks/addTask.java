package com.brains404.scheduler.ui.tasks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.brains404.scheduler.MainActivity;
import com.brains404.scheduler.R;

public class addTask extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
        String[] days = { "Math", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY","SATURDAY","SUNDAY"};
        String[] weeks= { "every Week","2 Weeks"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Change Theme
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);
        if(useDarkTheme) {
            setTheme(R.style.DarkAppTheme);
        }
        setContentView(R.layout.activity_add_task);
        // Back Home Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
            //Getting the instance of Spinner and applying OnItemSelectedListener on it
            Spinner spinSessions = (Spinner) findViewById(R.id.s_session);
            spinSessions.setOnItemSelectedListener(this);
        Spinner spinWeeks = (Spinner) findViewById(R.id.s_weeks);
        spinWeeks.setOnItemSelectedListener(this);
            //Creating the ArrayAdapter instance having the country list
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,days);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinSessions.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,R.layout.spinner_item,weeks);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinWeeks.setAdapter(arrayAdapter2);

        }
    // Back Button To App Bar(from addSession Activity => MainActivity)
    // TODO Back from addTask => Task Fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //TODO Added Tasks may not apear immediatly to be verified
              finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(getApplicationContext(),days[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 1) {
            //no fragments left
            finish();
        } else {
            super.onBackPressed();
        }

    }
}
