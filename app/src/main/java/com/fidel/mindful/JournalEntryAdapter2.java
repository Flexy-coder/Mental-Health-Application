package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JournalEntryAdapter2 extends RecyclerView.Adapter<JournalEntryAdapter2.ViewHolder> {

    private List<JournalEntry2> journalEntries;

    public JournalEntryAdapter2(List<JournalEntry2> journalEntries) {
        this.journalEntries = journalEntries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_journal_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JournalEntry2 entry = journalEntries.get(position);

        holder.titleTextView.setText(entry.getTitle());
        holder.contentTextView.setText(entry.getContent());

        // Format date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.dateTimeTextView.setText(dateFormat.format(entry.getDateTime()));

        // You can set other views here based on your layout

        // Note: Adjust the layout file (item_journal_entry.xml) according to your needs
    }

    @Override
    public int getItemCount() {
        return journalEntries.size();
    }

    public void addJournalEntry(JournalEntry2 entry) {
        journalEntries.add(entry);
        notifyItemInserted(journalEntries.size() - 1);
    }

    public void clearJournalEntries() {
        journalEntries.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView contentTextView;
        TextView dateTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.textViewEntryTitle);
            contentTextView = itemView.findViewById(R.id.textViewEntryContent);
            dateTimeTextView = itemView.findViewById(R.id.textViewDateTime);

            // You can find other views here based on your layout
        }
    }
}
