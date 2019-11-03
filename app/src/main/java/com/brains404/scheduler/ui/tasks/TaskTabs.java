package com.brains404.scheduler.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.R;
import com.brains404.scheduler.ui.time_table.SessionListViewAdapter;

import java.util.ArrayList;

public class TaskTabs extends Fragment {
    private String day;
    public TaskTabs(String day) {
        this.day=day;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.time_table_tabs, container, false);


        ArrayList<Session> sessionsList=new ArrayList<Session>() ;
        Session session = new Session(1,day+" Task","Tunisia","23:00","00:00","Monday");
        sessionsList.add(session);
        sessionsList.add(session);
        sessionsList.add(session);
        ListView lv = (ListView)root.findViewById(R.id.lv_sessions);
        SessionListViewAdapter customListViewAdapter;
        //TODO Create new task_item layout (instead of session_item)
        customListViewAdapter = new SessionListViewAdapter(getActivity(),R.layout.session_item,sessionsList);
        lv.setAdapter(customListViewAdapter );

        return root;
    }


}