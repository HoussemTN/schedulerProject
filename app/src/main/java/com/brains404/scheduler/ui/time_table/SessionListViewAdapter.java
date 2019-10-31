package com.brains404.scheduler.ui.time_table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.R;

import java.util.List;

public class SessionListViewAdapter extends ArrayAdapter<Session> {
    Context context ;
    int resource ;
    List<Session> sessionsList ;
    public SessionListViewAdapter(@NonNull Context context, int resource, @NonNull List<Session> sessionsList) {
        super(context, resource, sessionsList);
        this.context=context ;
        this.resource=resource;
        this.sessionsList=sessionsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Session session = sessionsList.get(position);
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.session_item,null);
        TextView startTime=view.findViewById(R.id.startTime);
        TextView endTime=view.findViewById(R.id.endTime) ;
        TextView sessionName=view.findViewById(R.id.name) ;
        sessionName.setText(session.getName());
        startTime.setText(session.getStartTime());
        endTime.setText(session.getEndTime());
        return view ;

    }
}
