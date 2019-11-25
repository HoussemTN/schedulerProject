package com.brains404.scheduler.ui.time_table;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TimePicker;
import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.MainActivity;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;


@SuppressWarnings("deprecation")
public class addSession extends AppCompatActivity {

    private TimePicker timeStartPicker;
    private TimePicker timeEndPicker;
    private Button btnChangeStartTime;
    private Button btnChangeEndTime;
    private EditText et_title;
    private EditText et_place;
    private int idDay;
    private int idSession ;
    private int endHour;
    private int endMinute;
    private int startHour;
    private int startMinute;
    private String title ;
    private String place ;
    private String startTime ;
    private String endTime ;

     final int START_TIME_DIALOG_ID = 1;
     final int END_TIME_DIALOG_ID = 2;
     final String CURRENT_DAY_ID="CURRENT_DAY_ID";
     final String LAST_VISITED_DAY_ID="LAST_VISITED_DAY_ID";
    SharedPreferences timeTablePrefs;
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_session);

        et_title = findViewById(R.id.et_title);
        et_place = findViewById(R.id.et_place);

        // Back Home Button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // Pick Time Method Call
        setCurrentTimeOnView();
        addListenerOnButton();
        timeTablePrefs = this.getSharedPreferences("timeTablePrefs", Context.MODE_PRIVATE);
        // get idDay from Main Activity (current tabLayout when clicking on fab)
        idDay=getIntent().getIntExtra(CURRENT_DAY_ID,0);
        Button btnAddSession = findViewById(R.id.btn_addSession);
        btnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate idSession
                idSession=(int)new Date().getTime();
                idSession=Math.abs(idSession);
                // retrieve EditTexts Values
                title=et_title.getText().toString();
                place=et_place.getText().toString();
                //get Start/End Time from buttons
                startTime=btnChangeStartTime.getText().toString();
                endTime=btnChangeEndTime.getText().toString();


                // title and place required
                if(!title.isEmpty() && !place.isEmpty()){
                    Session newSession= new Session(idSession,title,place,startTime,endTime,idDay);
                    SharedPreferences.Editor prefsEditor = timeTablePrefs.edit();
                    Gson gson = new Gson();
                    json= gson.toJson(newSession);
                    prefsEditor.putString(idSession+"", json);
                    prefsEditor.apply();
                    et_title.setText("");
                    et_place.setText("");
                    btnChangeStartTime.setText(getResources().getString(R.string.default_start_time));
                    btnChangeEndTime.setText(getResources().getString(R.string.default_end_time));
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_container),"Session Added Successfully",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // TODO delete this part
                    /* TESTING*/
                     Map<String, ?> allEntries = timeTablePrefs.getAll();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                    }
                    /*END TESTING*/
                }else{
                    //TODO add controls and error messages
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_container),"Title/Place required",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                // clear SharedPreferences for test purposes
               //    timeTablePrefs.edit().clear().apply();
            }
        });


    }
    //mapping
    public void setCurrentTimeOnView() {

        timeStartPicker =  findViewById(R.id.tp_start_time);
        timeEndPicker =  findViewById(R.id.tp_end_time);

        final Calendar c = Calendar.getInstance();
        // initialization of time pickers currentTime
        endHour = c.get(Calendar.HOUR_OF_DAY);
        endMinute = c.get(Calendar.MINUTE);
        startHour = c.get(Calendar.HOUR_OF_DAY)+1;
        startMinute = c.get(Calendar.MINUTE);
        //current Time for startTime and current +1Hour to EndTime
        timeStartPicker.setCurrentHour(startHour);
        timeStartPicker.setCurrentMinute(startMinute);
        timeEndPicker.setCurrentHour(startHour);
        timeEndPicker.setCurrentMinute(endMinute);
    }

    public void addListenerOnButton() {
       // mapping buttons
        btnChangeStartTime =  findViewById(R.id.btn_startTime);
        btnChangeEndTime =  findViewById(R.id.btn_endTime);
       // startTime Button event
        btnChangeStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(START_TIME_DIALOG_ID,null);

            }

        });
        //endTime Button event
        btnChangeEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(END_TIME_DIALOG_ID,null);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id,Bundle bundle) {
        switch (id) {
            case START_TIME_DIALOG_ID:
                //set time picker as current time
                return new TimePickerDialog(this,
                        timeStartPickerListener, startHour, startMinute,true);
            case END_TIME_DIALOG_ID:
                //set time picker as current time
                return new TimePickerDialog(this,
                        timeEndPickerListener, endHour, endMinute,true);

        }
        return null;
    }
    // onPicking Time draw Time for StartTime Button
    private TimePickerDialog.OnTimeSetListener timeStartPickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    startHour = selectedHour;
                    startMinute = selectedMinute;
                    //set current time into textview
                    btnChangeStartTime.setText(new StringBuilder().append(pad(startHour))
                            .append(":").append(pad(startMinute)));

                    //set current time into timepicker
                    timeStartPicker.setCurrentHour(startHour);
                    timeStartPicker.setCurrentMinute(startMinute);
                }
            };
    // onPicking Time draw Time for endTime Button
    private TimePickerDialog.OnTimeSetListener timeEndPickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    endHour = selectedHour;
                    endMinute = selectedMinute;

                    //set current time into TextView
                    btnChangeEndTime.setText(new StringBuilder().append(pad(endHour))
                            .append(":").append(pad(endMinute)));

                    //set current time into TimePicker
                    timeEndPicker.setCurrentHour(endHour);
                    timeEndPicker.setCurrentMinute(endMinute);
                }
            };


    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + c;
    }

    // Back Button To App Bar(from addSession Activity => MainActivity)
    // TODO Back from addSession => TimeTable Fragment(same idDay)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //DONE Send idDay back
            Intent intent = new Intent(addSession.this, MainActivity.class);
            intent.putExtra(LAST_VISITED_DAY_ID,idDay);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onBackPressed(){
        //DONE Send idDay back
        Intent intent = new Intent(addSession.this,MainActivity.class);
        intent.putExtra(LAST_VISITED_DAY_ID,idDay);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
