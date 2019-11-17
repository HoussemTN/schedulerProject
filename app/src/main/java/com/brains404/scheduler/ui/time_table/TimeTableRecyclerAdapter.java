package com.brains404.scheduler.ui.time_table;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brains404.scheduler.Entities.Session;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;



public class TimeTableRecyclerAdapter extends RecyclerView.Adapter<TimeTableRecyclerAdapter.ViewHolder>  {
    private ArrayList<Session> sessionData;
    class  ViewHolder extends RecyclerView.ViewHolder{
        TextView startTime;
        TextView endTime;
        TextView title;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime=itemView.findViewById(R.id.startTime);
            endTime=itemView.findViewById(R.id.endTime);
            title=itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar snackbar = Snackbar
                            .make(v.getRootView(), sessionData.get(position).getTitle(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TimeTableRecyclerAdapter(ArrayList<Session> sessionData) {
        this.sessionData = sessionData;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.startTime.setText(sessionData.get(position).getStartTime());
        holder.endTime.setText(sessionData.get(position).getEndTime());
        holder.title.setText(sessionData.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return sessionData.size();
    }



}
