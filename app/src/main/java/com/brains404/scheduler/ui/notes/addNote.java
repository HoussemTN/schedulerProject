package com.brains404.scheduler.ui.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.brains404.scheduler.Entities.Note;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Date;

public class addNote extends AppCompatActivity {
    EditText titleField ;
    EditText contentField;
    Button addNote ;
    String  title ;
    String content;
    int idNote;
    SharedPreferences notePrefs;
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Change Theme
        SharedPreferences preferences = getSharedPreferences("PrefsTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("darkTheme", false);
        if(useDarkTheme) {
            setTheme(R.style.DarkAppTheme);
        }
        setContentView(R.layout.activity_add_note);
        // Back Home Button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        titleField= findViewById(R.id.et_title_add_note);
        contentField=findViewById(R.id.et_content_add_note);
        addNote=findViewById(R.id.btn_addNote);
        notePrefs = this.getSharedPreferences("notePrefs", Context.MODE_PRIVATE);


        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate idNote
                idNote=(int)new Date().getTime();
                idNote=Math.abs(idNote);
                title = titleField.getText().toString();
                content = contentField.getText().toString();
                if(!title.isEmpty() && !content.isEmpty()){
                 Note newNote = new Note (idNote,title,content,new Date(),0);
                 SharedPreferences.Editor prefsEditor = notePrefs.edit();
                    Gson gson = new Gson();
                    json= gson.toJson(newNote);
                    prefsEditor.putString(idNote+"", json);
                    prefsEditor.apply();
                    // Show Message Session Added Successfully
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.rl_container_add_note),getResources().getString(R.string.note_added_success_message),Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });
    }
}
