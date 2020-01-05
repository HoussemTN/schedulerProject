package com.brains404.scheduler.ui.notes;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.brains404.scheduler.Entities.Note;
import com.brains404.scheduler.R;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;


public class NoteTabsFragment extends Fragment {
    // tab idDay
    private  int label ;

    public NoteTabsFragment(int label) {
        this.label=label;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.note_tabs, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rv_notes);
        ArrayList<Note> notesList=new ArrayList<>() ;
        SharedPreferences  notePrefs =this.getActivity().getSharedPreferences("notePrefs", Context.MODE_PRIVATE);


        Gson gson = new Gson();
        // Get all Keys to loop
        Set<String> keys = notePrefs.getAll().keySet();
        // Case Note cache not empty
        if (keys.size() > 0) {
            for (String key : keys) {
                String json = notePrefs.getString(key, "");
                Note myNote = gson.fromJson(json, Note.class);
                if (myNote.getIdLabel() == label) {
                    notesList.add(myNote);
                }

            }
            // Current Day empty
            if(notesList.size()==0){
                TextView noNoteMessage= root.findViewById(R.id.tv_empty_notes_rv_message);
                noNoteMessage.setText(getResources().getString(R.string.empty_note_message));
                recyclerView.setVisibility(View.GONE);
                noNoteMessage.setVisibility(View.VISIBLE);
            }
            //All Days Empty (No notes saved in timeTablePrefs)
        } else {
            TextView noNoteMessage= root.findViewById(R.id.tv_empty_notes_rv_message);
            noNoteMessage.setText(getResources().getString(R.string.empty_note_message));
            recyclerView.setVisibility(View.GONE);
            noNoteMessage.setVisibility(View.VISIBLE);

        }
        // Sort Note List
        Collections.sort(notesList,new Comparator<Note>() {
            @Override
            public int compare(Note N1, Note N2) {
                // descending sort
                return N1.getDateCreation().compareTo(N2.getDateCreation());
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new NoteRecyclerAdapter(notesList);
        recyclerView.setAdapter(adapter);
        return root;
    }


}