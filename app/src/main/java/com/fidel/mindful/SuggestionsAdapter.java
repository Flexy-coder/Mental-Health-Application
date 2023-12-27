package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.SuggestionViewHolder> {
    private List<String> suggestionList;
    private Context context;

    public SuggestionsAdapter(List<String> suggestionList) {
        this.suggestionList = suggestionList;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.suggestion_item, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        String suggestion = suggestionList.get(position);
        holder.bind(suggestion);
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    public void setSuggestionList(List<String> suggestionList) {
        this.suggestionList = suggestionList;
        notifyDataSetChanged();
    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        private TextView suggestionText;

        SuggestionViewHolder(View itemView) {
            super(itemView);
            suggestionText = itemView.findViewById(R.id.suggestionText);
        }

        void bind(String suggestion) {
            suggestionText.setText(suggestion);
        }
    }
}
