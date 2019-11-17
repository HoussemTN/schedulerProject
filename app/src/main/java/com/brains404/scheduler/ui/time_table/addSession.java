package com.brains404.scheduler.ui.time_table;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import com.brains404.scheduler.MainActivity;
import com.brains404.scheduler.R;
import java.util.Calendar;


@SuppressWarnings("deprecation")
public class addSession extends AppCompatActivity {

    private TimePicker timeStartPicker;
    private TimePicker timeEndPicker;
    private Button btnChangeStartTime;
    private Button btnChangeEndTime;
    private int endHour;
    private int endMinute;
    private int startHour;
    private int startMinute;
    static final int START_TIME_DIALOG_ID = 1;
    static final int END_TIME_DIALOG_ID = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        // Back Home Button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // Pick Time Method Call
        setCurrentTimeOnView();
        addListenerOnButton();


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

                    //set current time into textview
                    btnChangeEndTime.setText(new StringBuilder().append(pad(endHour))
                            .append(":").append(pad(endMinute)));

                    //set current time into timepicker
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
    // TODO Back from addSession => TimeTable Fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
