package com.brains404.scheduler.ui.tasks;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.brains404.scheduler.AlarmReceiver;
import com.brains404.scheduler.Entities.Task;
import com.brains404.scheduler.MainActivity;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;




@SuppressWarnings("deprecation")
public class addTask extends AppCompatActivity {

    private TimePicker timeStartPicker;
    private Button btnChangeStartTime;
    private EditText et_task_title;
    private EditText et_task_description;
    private CheckBox checkBox ;
    private int idDay;
    private int idTask ;
    private int startHour;
    private int startMinute;
    private int defaultStatus=0;
    private String title ;
    private String description ;
    private String startTime ;
    final int START_TIME_DIALOG_ID = 1;
    final String CURRENT_TASK_DAY_ID="CURRENT_TASK_DAY_ID";
    final String LAST_VISITED_TASK_DAY_ID="LAST_VISITED_TASK_DAY_ID";
    SharedPreferences taskPrefs;
    String json;

    private int showDelayInHours;
    private int showDelayInMinutes;
    private int showDelayInDays ;

    public final String CHANNEL_ID = "500" ;

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
        //EditTexts [title,place]
        et_task_title = findViewById(R.id.et_task_title);
        et_task_description = findViewById(R.id.et_task_description);

        //TimePickers Mapping
        timeStartPicker =  findViewById(R.id.tp_task_start_time);
        // Buttons show timepicker selected Time
        btnChangeStartTime =  findViewById(R.id.btn_task_startTime);
        // Checkbox Notify me
        checkBox = findViewById(R.id.ch_notify_task);

        // Back Home Button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // Pick Time Method Call
        setCurrentTimeOnView();
        addListenerOnButton();
        taskPrefs = this.getSharedPreferences("taskPrefs", Context.MODE_PRIVATE);
        // get idDay from Main Activity (current tabLayout when clicking on fab)
        idDay=getIntent().getIntExtra(LAST_VISITED_TASK_DAY_ID,0);
        Button btnAddTask = findViewById(R.id.btn_addTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate idSession
                idTask=(int)new Date().getTime();
                idTask=Math.abs(idTask);
                // retrieve EditTexts Values
                title=et_task_title.getText().toString();
                description=et_task_description.getText().toString();
                //get Start/End Time from buttons
                startTime=btnChangeStartTime.getText().toString();
                  boolean toNotify = checkBox.isChecked();

                // title and place required
                if(!title.isEmpty() && !description.isEmpty()&& !startTime.isEmpty() ){
                    Task newTask= new Task(idTask,title,description,startTime,defaultStatus,idDay);
                    SharedPreferences.Editor prefsEditor = taskPrefs.edit();
                    Gson gson = new Gson();
                    json= gson.toJson(newTask);
                    prefsEditor.putString(idTask+"", json);
                    prefsEditor.apply();
                    // Show Message Session Added Successfully
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_task_container),getResources().getString(R.string.task_added_success_message),Snackbar.LENGTH_LONG);
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
                    // Init Form
                    initForm();
                    // TODO delete this part
                    /* TESTING*/
                    Map<String, ?> allEntries = taskPrefs.getAll();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                    }
                    /*END TESTING*/
                    // Session Not added yet
                } else{
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_task_container),getResources().getString(R.string.task_required_message),Snackbar.LENGTH_LONG);
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
        btnChangeStartTime.setText(new StringBuilder().append(pad(startHour))
                .append(":").append(pad(startMinute)));

    }

    public void addListenerOnButton() {
        // mapping buttons

        // startTime Button event
        btnChangeStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(START_TIME_DIALOG_ID,null);

            }

        });


    }

    @Override
    protected Dialog onCreateDialog(int id,Bundle bundle) {
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
                    btnChangeStartTime.setText(new StringBuilder().append(pad(startHour))
                            .append(":").append(pad(startMinute)));

                    //set current time into timepicker
                    timeStartPicker.setCurrentHour(startHour);
                    timeStartPicker.setCurrentMinute(startMinute);
                }
            };
    // onPicking Time draw Time for endTime Button


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
            Intent intent = new Intent(addTask.this, MainActivity.class);
            intent.putExtra(LAST_VISITED_TASK_DAY_ID,idDay);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        //DONE Send idDay back
        Intent intent = new Intent(addTask.this,MainActivity.class);
        intent.putExtra(LAST_VISITED_TASK_DAY_ID,idDay);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
    // Notification Builder
    private Notification getNotification (Task task) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent notificationIntent = PendingIntent.getActivity(this, task.getIdTask(), intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, CHANNEL_ID) ;
        builder.setContentTitle(task.getTitle()) ;
        builder.setContentText(task.getDescription()) ;
        builder.setSmallIcon(R.drawable.ic_task) ;
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
    //Init View Data
    public void initForm(){

        et_task_title.setText("");
        et_task_description.setText("");
        //set current time into Button
        btnChangeStartTime.setText(new StringBuilder().append(pad(startHour))
                .append(":").append(pad(startMinute)));


    }
    //TODO handle Boot Device for AlarmManager
}
