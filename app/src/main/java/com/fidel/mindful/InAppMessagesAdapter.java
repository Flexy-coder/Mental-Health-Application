package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fidel.mindful.R; // Replace with the actual package name

import java.util.List;

public class InAppMessagesAdapter extends RecyclerView.Adapter<InAppMessagesAdapter.ViewHolder> {

    private List<InAppMessageData> inAppMessages;
    private Context context;

    public InAppMessagesAdapter(List<InAppMessageData> inAppMessages, Context context) {
        this.inAppMessages = inAppMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inapp_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InAppMessageData inAppMessage = inAppMessages.get(position);

        // Bind the in-app message data to the ViewHolder
        holder.titleTextView.setText(inAppMessage.getTitle());
        holder.contentTextView.setText(inAppMessage.getContent());

        // Add bindings for other fields as needed
    }

    @Override
    public int getItemCount() {
        return inAppMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView); // Replace with the actual ID
            contentTextView = itemView.findViewById(R.id.contentTextView); // Replace with the actual ID
        }
    }
}

