package com.brains404.scheduler.ui.tasks;


import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brains404.scheduler.Entities.Task;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder>  {
    private ArrayList<Task> taskData;
   private int previousExpandedPosition = -1;
   private int expandedPosition =-1 ;
    class  ViewHolder extends RecyclerView.ViewHolder{
        TextView startTime;
        TextView title;
        TextView description;
        RelativeLayout details ;
        Button edit ;
        Button done;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            startTime=itemView.findViewById(R.id.tv_task_startTime);
            title=itemView.findViewById(R.id.tv_task_title);
            description=itemView.findViewById(R.id.tv_task_description);
            details=itemView.findViewById(R.id.rl_task_expandable_container);
            edit=details.findViewById(R.id.btn_edit_task);
            done=details.findViewById(R.id.btn_done_task);

        itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  int position = getAdapterPosition();

                    Snackbar snackbar = Snackbar
                            .make(itemView, taskData.get(position).getTitle(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of ArrayList)
    TaskRecyclerAdapter(ArrayList<Task> taskData) {

        this.taskData = taskData;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.startTime.setText(taskData.get(position).getStartTime());
        holder.title.setText(taskData.get(position).getTitle());
        holder.description.setText(taskData.get(position).getDescription());
        //Handle Collapsed/Expanded Section
        final boolean isExpanded = position== expandedPosition;
        holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
    holder.edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(),editTask.class);
            intent.putExtra("idTask",taskData.get(position).getIdTask()+"");
            view.getContext().startActivity(intent);
        }
    });
  /*  holder.done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor taskPrefs;
            taskData.get(position).getIdTask();
        }
    });*/
        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                expandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskData.size();
    }


}
