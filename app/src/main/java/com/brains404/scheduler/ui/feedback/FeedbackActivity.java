package com.brains404.scheduler.ui.feedback;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class FeedbackActivity extends AppCompatActivity {
       EditText title ;
       EditText message ;
       Button send ;
       final String DATA_REFERENCE_NAME = "Feedback" ;
    boolean isDarkTheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);

        if(useDarkTheme) {
            setTheme(R.style.DarkAppTheme);
            isDarkTheme = true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //Back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    title= findViewById(R.id.et_subject);
    message= findViewById(R.id.et_message);
    send= findViewById(R.id.btn_send_feedback);


        send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (internetConnectionAvailable()){


           //check EditTexts not Empty
            if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {
                if(title.getText().length() < 50 && message.getText().length()<255) {

                    // Write to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference(DATA_REFERENCE_NAME);

                    databaseReference.child(System.currentTimeMillis() + "").child(title.getText().toString()).setValue(message.getText().toString());
                    initFeedbackForm();
                    // Success Message
                    Snackbar snackbar=Snackbar.make(view,getResources().getString(R.string.feedback_sent_success_message),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    // Message Too Long
                   final  Snackbar snackbar=Snackbar.make(view,getResources().getString(R.string.feedback_message_too_long),Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(getResources().getString(R.string.btn_dismiss_action), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          //dismiss snackBar
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }

            } else{
                //Required input
                Snackbar snackbar=Snackbar.make(view,getResources().getString(R.string.feedback_required_message),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            }else {
                //Offline
                Snackbar snackbar=Snackbar.make(view,getResources().getString(R.string.feedback_internet_offline_message),Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    });
    }
    // check if device connected to network (doesn't mean that the device is connected to internet)
    // ping to google.com to make sure that the device is connected to internet
    private boolean internetConnectionAvailable() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            // timeout period 1000 Millis
            inetAddress = future.get(1000, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException a) {
            Log.i("connectivity","Interrupted");
        } catch (ExecutionException b) {
            Log.i("connectivity","Not executed");
        } catch (TimeoutException c) {
            Log.i("connectivity","Timeout");
        }
        return inetAddress!=null && !inetAddress.toString().isEmpty();
    }
    public void initFeedbackForm(){
        title.setText("");
        message.setText("");
    }
}
