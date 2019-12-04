package com.brains404.scheduler.ui.tasks;

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

import com.brains404.scheduler.Entities.Task;
import com.brains404.scheduler.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;


public class TaskTabs extends Fragment {
    private int day;
   TaskTabs(int day) {
        this.day=day;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.task_tabs, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.rv_tasks);
        ArrayList<Task> tasksList=new ArrayList<>() ;
        SharedPreferences taskPrefs =this.getActivity().getSharedPreferences("taskPrefs", Context.MODE_PRIVATE);


        Gson gson = new Gson();
        // Get all Keys to loop
        Set<String> keys = taskPrefs.getAll().keySet();
        // Case Session cache not empty
        if (keys.size() > 0) {
            Iterator<String> itr = keys.iterator();
            while(itr.hasNext()){
                String json = taskPrefs.getString(itr.next(), "");
                Task myTask = gson.fromJson(json, Task.class);
                if(myTask.getIdDay()==day && myTask.getStatus()==0) {
                    tasksList.add(myTask);
                }

            }
            // Current Day empty
            if(tasksList.size()==0){
                TextView noTaskMessage= root.findViewById(R.id.tv_empty_tasks_rv_message);
                noTaskMessage.setText(getResources().getString(R.string.empty_task_message));
                recyclerView.setVisibility(View.GONE);
                noTaskMessage.setVisibility(View.VISIBLE);
            }
            //All Days Empty (No sessions saved in timeTablePrefs)
        } else {
            TextView noTaskMessage= root.findViewById(R.id.tv_empty_tasks_rv_message);
            noTaskMessage.setText(getResources().getString(R.string.empty_task_message));
            recyclerView.setVisibility(View.GONE);
            noTaskMessage.setVisibility(View.VISIBLE);

        }

        Collections.sort(tasksList,new Comparator<Task>() {
            @Override
            public int compare(Task T1, Task T2) {
                // get Hours from sessions to compare
                int T1_startHour=Integer.valueOf(T1.getStartTime().substring(0,2));
                int T2_startHour=Integer.valueOf(T2.getStartTime().substring(0,2));
                // get Minutes from sessions to compare
                int T1_startMinutes=Integer.valueOf(T1.getStartTime().substring(T1.getStartTime().indexOf(":") + 1));
                int T2_startMinutes=Integer.valueOf(T2.getStartTime().substring(T2.getStartTime().indexOf(":") + 1));

                Calendar c1 = Calendar.getInstance();
                c1.setTimeInMillis(System.currentTimeMillis());
                c1.set(Calendar.HOUR_OF_DAY, T1_startHour);
                c1.set(Calendar.MINUTE, T1_startMinutes);

                Calendar c2 = Calendar.getInstance();
                c2.setTimeInMillis(System.currentTimeMillis());
                c2.set(Calendar.HOUR_OF_DAY, T2_startHour);
                c2.set(Calendar.MINUTE, T2_startMinutes);

                // descending sort
                return c1.getTime().compareTo(c2.getTime());
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new TaskRecyclerAdapter(tasksList);
        recyclerView.setAdapter(adapter);
        return root;
    }


}