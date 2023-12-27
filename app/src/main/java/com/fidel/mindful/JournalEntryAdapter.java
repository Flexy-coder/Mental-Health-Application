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

public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.JournalEntryViewHolder> {

    private final List<JournalEntry> journalEntryList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public JournalEntryAdapter(List<JournalEntry> journalEntryList) {
        this.journalEntryList = journalEntryList;
    }

    @NonNull
    @Override
    public JournalEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journal_entry, parent, false);
        return new JournalEntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalEntryViewHolder holder, int position) {
        JournalEntry currentEntry = journalEntryList.get(position);

        // Set the entry title
        holder.textViewEntryTitle.setText(currentEntry.getTitle());

        // Set the entry content
        holder.textViewEntryContent.setText(currentEntry.getContent());

        // Set the entry date and time
        String formattedDateTime = dateFormat.format(currentEntry.getDateTime());
        holder.textViewDateTime.setText(formattedDateTime);

    }

    @Override
    public int getItemCount() {
        return journalEntryList.size();
    }

    public void addJournalEntry(JournalEntry entry) {
        journalEntryList.add(entry);
        notifyItemInserted(journalEntryList.size() - 1);
    }

    public void clearJournalEntries() {
        journalEntryList.clear();
        notifyDataSetChanged();
    }

    static class JournalEntryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEntryTitle;
        TextView textViewEntryContent;
        TextView textViewDateTime;
        TextView textViewSentiment;

        JournalEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEntryTitle = itemView.findViewById(R.id.textViewEntryTitle);
            textViewEntryContent = itemView.findViewById(R.id.textViewEntryContent);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);

        }
    }
}
