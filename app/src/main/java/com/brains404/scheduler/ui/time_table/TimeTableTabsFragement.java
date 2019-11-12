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

public class TimeTableTabsFragement extends Fragment {
    private String day;
    public TimeTableTabsFragement(String day) {
    this.day=day;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.time_table_tabs, container, false);


            ArrayList<Session> sessionsList=new ArrayList<Session>() ;
        Session session3 = new Session(1,"Math Session","Tunisia","11:00","12:00","Monday");
        Session session = new Session(3,"English Session","Tunisia","09:00","10:00","Monday");
        Session session2 = new Session(2,"Arabic Session","Tunisia","10:00","11:00","Monday");
        sessionsList.add(session);
        sessionsList.add(session2);
        sessionsList.add(session3);

            ListView lv = (ListView)root.findViewById(R.id.lv_sessions);

        SessionListViewAdapter customListViewAdapter;
        customListViewAdapter = new SessionListViewAdapter(getActivity(),R.layout.session_item,sessionsList);
        lv.setAdapter(customListViewAdapter );
            return root;
    }


}