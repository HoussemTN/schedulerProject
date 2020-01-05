package com.brains404.scheduler.ui.notes;




import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brains404.scheduler.Entities.Note;
import com.brains404.scheduler.R;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;



public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>  {
    private ArrayList<Note> noteData;
    private int previousExpandedPosition = -1;
    private int expandedPosition =-1 ;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
       // RelativeLayout details ;
      //  Button edit ;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.tv_note_title);
            content=itemView.findViewById(R.id.tv_note_content);
           // details=itemView.findViewById(R.id.rl_expandable_container);
          //  edit=details.findViewById(R.id.btn_edit_session);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar snackbar = Snackbar
                            .make(itemView, noteData.get(position).getTitle(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of ArrayList)
    NoteRecyclerAdapter(ArrayList<Note> sessionData) {

        this.noteData = sessionData;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public NoteRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        return new NoteRecyclerAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NoteRecyclerAdapter.ViewHolder holder, final int position) {


        holder.title.setText(noteData.get(position).getTitle());
        holder.content.setText(noteData.get(position).getContent());
        //Handle Collapsed/Expanded Section
        final boolean isExpanded = position== expandedPosition;
       // holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
      /*  holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), editNote.class);
                intent.putExtra("idNote",sessionData.get(position).getIdNote()+"");
                view.getContext().startActivity(intent);
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
        return noteData.size();
    }


}
