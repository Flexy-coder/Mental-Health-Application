package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<NoteItem> noteItemList;

    public NoteAdapter(List<NoteItem> noteItemList) {
        this.noteItemList = noteItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteItem noteItem = noteItemList.get(position);
        holder.clientNameTextView.setText(noteItem.getClientName());
        holder.sessionDataTextView.setText(noteItem.getSessionData());
        holder.notesTextView.setText(noteItem.getNotes());
    }

    @Override
    public int getItemCount() {
        return noteItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clientNameTextView;
        TextView sessionDataTextView;
        TextView notesTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clientNameTextView = itemView.findViewById(R.id.textViewClientName);
            sessionDataTextView = itemView.findViewById(R.id.textViewSessionData);
            notesTextView = itemView.findViewById(R.id.textViewNotes);
        }
    }
}

