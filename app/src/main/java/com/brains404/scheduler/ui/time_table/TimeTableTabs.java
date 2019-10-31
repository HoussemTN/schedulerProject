package com.brains404.scheduler.ui.time_table;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.R;

import java.util.ArrayList;

public class TimeTableTabs extends Fragment {
    private String day;
    public TimeTableTabs(String day) {
    this.day=day;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.time_table_tabs, container, false);


            ArrayList<Session> sessionsList=new ArrayList<Session>() ;
            Session session = new Session(1,day+" TimeTable subject","Tunisia","23:00","00:00","Monday");
            sessionsList.add(session);
            sessionsList.add(session);
            sessionsList.add(session);
            ListView lv = (ListView)root.findViewById(R.id.lv_sessions);

        SessionListViewAdapter customListViewAdapter;
        customListViewAdapter = new SessionListViewAdapter(getActivity(),R.layout.session_item,sessionsList);
        lv.setAdapter(customListViewAdapter );

            return root;
    }


}