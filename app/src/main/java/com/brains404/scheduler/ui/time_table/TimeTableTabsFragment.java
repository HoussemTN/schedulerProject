package com.brains404.scheduler.ui.time_table;

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
import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.R;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;


public class TimeTableTabsFragment extends Fragment {
     // tab idDay
     private final int day ;
     private final String TIMETABLE_PREF_NAME="timeTablePrefs";
     TimeTableTabsFragment(int day) {
         this.day=day;
     }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.time_table_tabs, container, false);
       RecyclerView recyclerView = root.findViewById(R.id.rv_sessions);
            ArrayList<Session> sessionsList=new ArrayList<>() ;
        SharedPreferences  timeTablePrefs =this.getActivity().getSharedPreferences(TIMETABLE_PREF_NAME, Context.MODE_PRIVATE);


        Gson gson = new Gson();
        // Get all Keys to loop
        Set<String> keys = timeTablePrefs.getAll().keySet();
        // Case Session cache not empty
        if (keys.size() > 0) {
            Iterator<String> itr = keys.iterator();
            while(itr.hasNext()){
                String json = timeTablePrefs.getString(itr.next(), "");
                Session mySession = gson.fromJson(json, Session.class);
                  if(mySession.getIdDay()==day) {
                      sessionsList.add(mySession);
                  }

            }
            // Current Day empty
            if(sessionsList.size()==0){
                TextView noSessionMessage= root.findViewById(R.id.tv_empty_sessions_rv_message);
                noSessionMessage.setText(getResources().getString(R.string.empty_session_message));
                recyclerView.setVisibility(View.GONE);
                noSessionMessage.setVisibility(View.VISIBLE);
            }
            //All Days Empty (No sessions saved in timeTablePrefs)
        } else {
           TextView noSessionMessage= root.findViewById(R.id.tv_empty_sessions_rv_message);
           noSessionMessage.setText(getResources().getString(R.string.empty_session_message));
           recyclerView.setVisibility(View.GONE);
           noSessionMessage.setVisibility(View.VISIBLE);

        }
            // Sessions Custom Comparator
            Collections.sort(sessionsList,new Comparator<Session>() {
                @Override
                public int compare(Session S1, Session S2) {
                    // get Hours from sessions to compare
                    int S1_startHour=Integer.valueOf(S1.getStartTime().substring(0,2));
                    int S2_startHour=Integer.valueOf(S2.getStartTime().substring(0,2));

                    // get Minutes from sessions to compare
                    int S1_startMinutes=Integer.valueOf(S1.getStartTime().substring(S1.getStartTime().indexOf(":") + 1));
                    int S2_startMinutes=Integer.valueOf(S2.getStartTime().substring(S2.getStartTime().indexOf(":") + 1));
                    // -1 for descending sort
                    return S1_startHour < S2_startHour ? -1 : (S2_startMinutes < S1_startMinutes ) ? 1 : 0;
                }
            });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new TimeTableRecyclerAdapter(sessionsList);
        recyclerView.setAdapter(adapter);
            return root;
    }


}