package com.brains404.scheduler.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.Entities.Task;
import com.brains404.scheduler.R;
import com.brains404.scheduler.ui.time_table.SessionListViewAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class TaskTabs extends Fragment {
    private String day;
   TaskTabs(String day) {
        this.day=day;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.task_tabs, container, false);


        ArrayList<Session> sessionsList=new ArrayList<>() ;
        Session session3 = new Session(1,"Math Problem","Tunisia","23:05","23:30",1);
        Session session = new Session(3,"English Homework","Tunisia","19:00","19:30",2);
        Session session2 = new Session(2,"Arabic Exercise","Tunisia","22:45","23:00",3);
        sessionsList.add(session);
        sessionsList.add(session2);
        sessionsList.add(session3);
        ListView lv = root.findViewById(R.id.lv_tasks);
        SessionListViewAdapter customListViewAdapter;
        //TODO Create new task_item layout (instead of session_item)
        customListViewAdapter = new SessionListViewAdapter(Objects.requireNonNull(getActivity()),R.layout.task_item,sessionsList);
        lv.setAdapter(customListViewAdapter );

        return root;
    }


}