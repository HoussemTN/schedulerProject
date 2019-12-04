package com.brains404.scheduler.ui.tasks;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.brains404.scheduler.AlarmReceiver;
import com.brains404.scheduler.Entities.Task;
import com.brains404.scheduler.MainActivity;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Calendar;


public class editTask extends AppCompatActivity {
       EditText et_task_title ;
       EditText et_task_description ;
       TimePicker timeStartPicker ;
       Button btnChangedStartTime;
       CheckBox checkBox ;
       Button delete ;
       Button edit;
       Task myTask;
    SharedPreferences taskPrefs;
    String idTaskString;
    private int idTask ;
    private int startHour;
    private int startMinute;
    private String title ;
    private String description ;
    private String startTime ;
    String json;

    final int START_TIME_DIALOG_ID = 1;
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
        setContentView(R.layout.activity_edit_task);

        et_task_title = findViewById(R.id.et_task_title_edit);
        et_task_description = findViewById(R.id.et_task_description_edit);

        //TimePickers Mapping
        timeStartPicker =  findViewById(R.id.tp_task_start_time_edit);
        // Buttons show timepicker selected Time
        btnChangedStartTime =  findViewById(R.id.btn_task_startTime_edit);
        // Checkbox Notify me
        checkBox = findViewById(R.id.ch_notify_task_edit);
        delete=findViewById(R.id.btn_editTask_delete);
        edit=findViewById(R.id.btn_editTask_edit);

        // Back Home Button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

       Intent i =getIntent();
        Bundle extras = i.getExtras();
        if (extras!=null){
        idTaskString= getIntent().getStringExtra("idTask");
        taskPrefs=this.getSharedPreferences("taskPrefs",MODE_PRIVATE);
        Gson gson = new Gson();
        // Get all Key
            if(taskPrefs.contains(idTaskString)) {
                // Case Session cache not empty
              json = taskPrefs.getString(idTaskString, "");
                 myTask = gson.fromJson(json, Task.class);

                    et_task_title.setText(myTask.getTitle());
                    et_task_description.setText(myTask.getDescription());
                    checkBox.setChecked(true);
                    btnChangedStartTime.setText(myTask.getStartTime());


            }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taskPrefs.edit().remove(idTaskString).apply();
                    //TODO cancel notification of this session
                    //DONE Send idDay back
                    Intent intent = new Intent(editTask.this, MainActivity.class);
                    intent.putExtra(LAST_VISITED_DAY_ID,idDay);
                    startActivity(intent);

                }
            });
        }
        setCurrentTimeOnView();
        addListenerOnButton();

        // get idDay from Main Activity (current tabLayout when clicking on fab)
        idDay=myTask.getIdDay();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate idTask
                idTask=myTask.getIdTask();
                // retrieve EditTexts Values
                title=et_task_title.getText().toString();
                description=et_task_description.getText().toString();
                //get Start/End Time from buttons
                startTime=btnChangedStartTime.getText().toString();
                 boolean toNotify = checkBox.isChecked();

                // title and place required
                if(!title.isEmpty() && !description.isEmpty()&& !startTime.isEmpty() ){
                    Task newTask= new Task(idTask,title,description,startTime,0,idDay);
                    SharedPreferences.Editor prefsEditor = taskPrefs.edit();
                    Gson gson = new Gson();
                    json= gson.toJson(newTask);
                    prefsEditor.putString(idTask+"", json);
                    prefsEditor.apply();
                    //TODO Show Message Task Updated Successfully
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_task_container_edit),getResources().getString(R.string.task_updated_success_message),Snackbar.LENGTH_LONG);
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
                        scheduleNotification(getNotification(newTask), delay, newTask.getIdTask());

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_set_message) + delayMessage, Toast.LENGTH_SHORT).show();

                    }



                    // Session Not Updated yet
                } else{
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_task_container_edit),getResources().getString(R.string.task_required_message),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });


    }
    //mapping
    public void setCurrentTimeOnView() {


        final Calendar c = Calendar.getInstance();
        // initialization of time pickers currentTime
        startHour = c.get(Calendar.HOUR_OF_DAY);
        startMinute = c.get(Calendar.MINUTE);
        //current Time for startTime and current +1Hour to EndTime
        timeStartPicker.setCurrentHour(startHour);
        timeStartPicker.setCurrentMinute(startMinute);

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




    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle bundle) {
        if (id == START_TIME_DIALOG_ID) {//set time picker as current time
            return new TimePickerDialog(this,
                    timeStartPickerListener, startHour, startMinute, true);
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
            Intent intent = new Intent(editTask.this, MainActivity.class);
            intent.putExtra(LAST_VISITED_DAY_ID,idDay);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        //DONE Send idDay back
        Intent intent = new Intent(editTask.this,MainActivity.class);
        intent.putExtra(LAST_VISITED_DAY_ID,idDay);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    // Notification Builder
    private Notification getNotification (Task task) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent notificationIntent = PendingIntent.getActivity(this, task.getIdTask(), intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "404") ;
        builder.setContentTitle(task.getTitle()) ;
        builder.setContentText(task.getDescription()) ;
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

