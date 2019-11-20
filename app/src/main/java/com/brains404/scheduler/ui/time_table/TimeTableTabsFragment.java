package com.brains404.scheduler.ui.time_table;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class TimeTableTabsFragment extends Fragment {
    String day ;
     RecyclerView recyclerView;
     RecyclerView.LayoutManager layoutManager;
     RecyclerView.Adapter adapter;
     TimeTableTabsFragment(String day) {
         this.day=day;
     }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.time_table_tabs, container, false);
        recyclerView = root.findViewById(R.id.rv_sessions);

            ArrayList<Session> sessionsList=new ArrayList<>() ;
        SharedPreferences  timeTablePrefs =this.getActivity().getSharedPreferences("timeTablePrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        // Get all Keys to loop
        Set<String> keys = timeTablePrefs.getAll().keySet();
        // Case Session cache not empty
        if (keys.size() > 0) {
            Iterator<String> itr = keys.iterator();
            while(itr.hasNext()){
                String json = timeTablePrefs.getString(itr.next(), "");
                Session mySession = gson.fromJson(json, Session.class);
                sessionsList.add(mySession);

            }
            //Empty Recycler View
        }else {
           TextView noSessionMessage= root.findViewById(R.id.tv_empty_sessions_rv_message);
           noSessionMessage.setText(getResources().getString(R.string.empty_session_message));
           recyclerView.setVisibility(View.GONE);
           noSessionMessage.setVisibility(View.VISIBLE);


        }


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TimeTableRecyclerAdapter(sessionsList);
        recyclerView.setAdapter(adapter);
            return root;
    }


}