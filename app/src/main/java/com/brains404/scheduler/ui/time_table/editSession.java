package com.brains404.scheduler.ui.time_table;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.brains404.scheduler.AlarmReceiver;
import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.MainActivity;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import java.util.Calendar;



public class editSession extends AppCompatActivity {
       EditText et_title ;
       EditText et_place ;
       TimePicker timeStartPicker ;
       TimePicker timeEndPicker ;
       Button btnChangedStartTime;
       Button btnChangedEndTime;
       CheckBox checkBox ;
       Button delete ;
       Button edit;
       Session mySession;
    SharedPreferences timeTablePrefs;
    String idSessionString;
    private int idSession ;
    private int endHour;
    private int endMinute;
    private int startHour;
    private int startMinute;
    private String title ;
    private String place ;
    private String startTime ;
    private String endTime ;
    String json;

    final int START_TIME_DIALOG_ID = 1;
    final int END_TIME_DIALOG_ID = 2;
    final String LAST_VISITED_DAY_ID="LAST_VISITED_DAY_ID";
    int idDay;
    private int showDelayInHours;
    private int showDelayInMinutes;
    private int showDelayInDays ;

    public final String CHANNEL_ID = "500" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Change Theme
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);
        if(useDarkTheme) {
            setTheme(R.style.DarkAppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);

        et_title = findViewById(R.id.et_title_edit);
        et_place = findViewById(R.id.et_place_edit);

        //TimePickers Mapping
        timeStartPicker =  findViewById(R.id.tp_start_time_edit);
        timeEndPicker =  findViewById(R.id.tp_end_time_edit);
        // Buttons show timepicker selected Time
        btnChangedStartTime =  findViewById(R.id.btn_startTime_edit);
        btnChangedEndTime =  findViewById(R.id.btn_endTime_edit);
        // Checkbox Notify me
        checkBox = findViewById(R.id.ch_notify_session_edit);
        delete=findViewById(R.id.btn_editSession_delete);
        edit=findViewById(R.id.btn_editSession_edit);

        // Back Home Button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

       Intent i =getIntent();
        Bundle extras = i.getExtras();
        if (extras!=null){
        idSessionString= getIntent().getStringExtra("idSession");
        timeTablePrefs=this.getSharedPreferences("timeTablePrefs",MODE_PRIVATE);
        Gson gson = new Gson();
        // Get all Key
            if(timeTablePrefs.contains(idSessionString)) {
                // Case Session cache not empty
              json = timeTablePrefs.getString(idSessionString, "");
                 mySession = gson.fromJson(json, Session.class);

                    et_title.setText(mySession.getTitle());
                    et_place.setText(mySession.getPlace());
                    checkBox.setChecked(true);
                    btnChangedStartTime.setText(mySession.getStartTime());
                    btnChangedEndTime.setText(mySession.getEndTime());


            }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeTablePrefs.edit().remove(idSessionString).apply();
                    //TODO cancel notification of this session
                    //DONE Send idDay back
                    Intent intent = new Intent(editSession.this, MainActivity.class);
                    intent.putExtra(LAST_VISITED_DAY_ID,idDay);
                    startActivity(intent);

                }
            });
        }
        setCurrentTimeOnView();
        addListenerOnButton();

        // get idDay from Main Activity (current tabLayout when clicking on fab)
        idDay=mySession.getIdDay();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate idSession
                idSession=mySession.getIdSession();
                // retrieve EditTexts Values
                title=et_title.getText().toString();
                place=et_place.getText().toString();
                //get Start/End Time from buttons
                startTime=btnChangedStartTime.getText().toString();
                endTime=btnChangedEndTime.getText().toString();
                boolean toNotify = checkBox.isChecked();

                // title and place required
                if(!title.isEmpty() && !place.isEmpty()&& !startTime.isEmpty() && !endTime.isEmpty()){
                    Session newSession= new Session(idSession,title,place,startTime,endTime,idDay);
                    SharedPreferences.Editor prefsEditor = timeTablePrefs.edit();
                    Gson gson = new Gson();
                    json= gson.toJson(newSession);
                    prefsEditor.putString(idSession+"", json);
                    prefsEditor.apply();
                    //TODO Show Message Session Updated Successfully
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_container_edit),getResources().getString(R.string.session_updated_success_message),Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // Set Alarm if checkbox Notify Me is checked
                    if(toNotify) {
                        //Calculate the delay
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(System.currentTimeMillis());
                        // Sunday == 0
                        c.set(Calendar.DAY_OF_WEEK, idDay + 2);
                        c.set(Calendar.HOUR_OF_DAY, startHour);
                        c.set(Calendar.MINUTE, startMinute);

                        long delay = c.getTimeInMillis();
                        //Validate delay
                        if (delay < System.currentTimeMillis()) {
                            // Current >> scheduled Time so the session is scheduled for next week (delay+weekPeriod[604800000])
                            delay = delay + 604800000;
                        }
                        showDelayInMinutes = (int) (delay - System.currentTimeMillis()) / 60000;
                        // example (35m)
                        String delayMessage = showDelayInMinutes + getResources().getString(R.string.minute_character);
                        if (showDelayInMinutes >= 60) {
                            showDelayInHours = showDelayInMinutes / 60;
                            showDelayInMinutes = showDelayInMinutes % 60;
                            // example (2h 35m)
                            delayMessage = showDelayInHours + getResources().getString(R.string.hour_character) + " " + showDelayInMinutes + getResources().getString(R.string.minute_character);
                        }
                        // example (1d 2h 35m)
                        if (showDelayInHours >= 24) {
                            showDelayInDays = showDelayInHours / 24;
                            showDelayInHours = showDelayInHours % 24;
                            delayMessage = showDelayInDays + getResources().getString(R.string.day_character) + " " + showDelayInHours + getResources().getString(R.string.hour_character) + " " + showDelayInMinutes + getResources().getString(R.string.minute_character);
                        }

                        // Create Notification then schedule it
                        scheduleNotification(getNotification(newSession), delay, newSession.getIdSession());

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_set_message) + delayMessage, Toast.LENGTH_SHORT).show();

                    }



                    // Session Not Updated yet
                } else{
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_container_edit),getResources().getString(R.string.session_required_message),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });


    }
    //mapping
    public void setCurrentTimeOnView() {


        final Calendar c = Calendar.getInstance();
        // initialization of time pickers currentTime
        endHour = c.get(Calendar.HOUR_OF_DAY)+1;
        endMinute = c.get(Calendar.MINUTE);
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);
        //current Time for startTime and current +1Hour to EndTime
        timeStartPicker.setCurrentHour(startHour);
        timeStartPicker.setCurrentMinute(startMinute);
        timeEndPicker.setCurrentHour(startHour);
        timeEndPicker.setCurrentMinute(endMinute);

    }
    public void addListenerOnButton() {
        // mapping buttons

        // startTime Button event
        btnChangedStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(START_TIME_DIALOG_ID,null);

            }

        });
        //endTime Button event
        btnChangedEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(END_TIME_DIALOG_ID,null);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle bundle) {
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
                    btnChangedStartTime.setText(new StringBuilder().append(pad(startHour))
                            .append(":").append(pad(startMinute)));

                    //set current time into time picker
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
                    btnChangedEndTime.setText(new StringBuilder().append(pad(endHour))
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //DONE Send idDay back
            Intent intent = new Intent(editSession.this, MainActivity.class);
            intent.putExtra(LAST_VISITED_DAY_ID,idDay);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        //DONE Send idDay back
        Intent intent = new Intent(editSession.this,MainActivity.class);
        intent.putExtra(LAST_VISITED_DAY_ID,idDay);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    // Notification Builder
    private Notification getNotification (Session session) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent notificationIntent = PendingIntent.getActivity(this, session.getIdSession(), intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "404") ;
        builder.setContentTitle(session.getTitle()) ;
        builder.setContentText(session.getPlace()) ;
        builder.setSmallIcon(R.drawable.ic_event_note ) ;
        builder.setContentIntent(notificationIntent);
        builder.setAutoCancel(true) ;
        builder.setSound(alarmSound);
        long[] pattern = {10,10,10,20,20,100,500};
        builder.setVibrate(pattern);
        builder.setChannelId(CHANNEL_ID) ;
        return builder.build() ;
    }
    //Notification scheduler
    private void scheduleNotification (Notification notification , long delay,int idNotification) {
        Intent notificationIntent = new Intent( this, AlarmReceiver. class ) ;
        // idNotification = idSession
        notificationIntent.putExtra("notificationId" ,idNotification) ;
        notificationIntent.putExtra("notification" , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, idNotification , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT) ;
        // Alarm Service
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE) ;
        assert alarmManager != null;
        // Repeat Alarm every Week
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, delay,
                AlarmManager.INTERVAL_DAY*7, pendingIntent);

        if (Build.VERSION.SDK_INT >= 23) {
            // Wakes up the device in Doze Mode
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,delay, pendingIntent);


        } else if (Build.VERSION.SDK_INT >= 19) {
            // Wakes up the device in Idle Mode
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,delay, pendingIntent);

        } else {
            // Old APIs
            alarmManager.set(AlarmManager.RTC_WAKEUP,delay, pendingIntent);
        }

    }


}

