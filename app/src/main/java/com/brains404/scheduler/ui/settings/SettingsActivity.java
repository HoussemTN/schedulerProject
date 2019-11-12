package com.brains404.scheduler.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;




import com.brains404.scheduler.R;

public class SettingsActivity extends AppCompatActivity {
     Boolean isDarkTheme =false;
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

        Switch  toggle= (Switch) findViewById(R.id.myswitch);
        toggle.setChecked(isDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {

                toggleTheme(isChecked);

            }
        });

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