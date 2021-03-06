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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;


public class TimeTableTabsFragment extends Fragment {
    // tab idDay
    private  int day ;

    TimeTableTabsFragment(int day) {
        this.day=day;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.time_table_tabs, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rv_sessions);
        ArrayList<Session> sessionsList=new ArrayList<>() ;
        SharedPreferences  timeTablePrefs =this.getActivity().getSharedPreferences("timeTablePrefs", Context.MODE_PRIVATE);


        Gson gson = new Gson();
        // Get all Keys to loop
        Set<String> keys = timeTablePrefs.getAll().keySet();
        // Case Session cache not empty
        if (keys.size() > 0) {
            for (String key : keys) {
                String json = timeTablePrefs.getString(key, "");
                Session mySession = gson.fromJson(json, Session.class);
                if (mySession.getIdDay() == day) {
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
        // Sort Session List
        Collections.sort(sessionsList,new Comparator<Session>() {
            @Override
            public int compare(Session S1, Session S2) {
                // get Hours from sessions to compare
                int S1_startHour=Integer.valueOf(S1.getStartTime().substring(0,2));
                int S2_startHour=Integer.valueOf(S2.getStartTime().substring(0,2));
                // get Minutes from sessions to compare
                int S1_startMinutes=Integer.valueOf(S1.getStartTime().substring(S1.getStartTime().indexOf(":") + 1));
                int S2_startMinutes=Integer.valueOf(S2.getStartTime().substring(S2.getStartTime().indexOf(":") + 1));

                Calendar c1 = Calendar.getInstance();
                c1.setTimeInMillis(System.currentTimeMillis());
                c1.set(Calendar.HOUR_OF_DAY, S1_startHour);
                c1.set(Calendar.MINUTE, S1_startMinutes);

                Calendar c2 = Calendar.getInstance();
                c2.setTimeInMillis(System.currentTimeMillis());
                c2.set(Calendar.HOUR_OF_DAY, S2_startHour);
                c2.set(Calendar.MINUTE, S2_startMinutes);

                // descending sort
              return c1.getTime().compareTo(c2.getTime());
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new TimeTableRecyclerAdapter(sessionsList);
        recyclerView.setAdapter(adapter);
        return root;
    }


}