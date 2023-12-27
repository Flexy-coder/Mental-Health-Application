package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessages> chatMessageList;

    // Constructor to initialize the chat message list
    public ChatAdapter(List<ChatMessages> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessages chatMessage = chatMessageList.get(position);
        return chatMessage.isUser() ? 1 : 2;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the appropriate layout based on the view type
        if (viewType == 1) {
            View userMessageView = inflater.inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(userMessageView);
        } else {
            View chatbotMessageView = inflater.inflate(R.layout.item_chatbot_message, parent, false);
            return new ChatbotMessageViewHolder(chatbotMessageView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ChatMessages chatMessage = chatMessageList.get(position);

        // Bind the data to the ViewHolder based on the view type
        if (viewHolder instanceof UserMessageViewHolder) {
            UserMessageViewHolder userViewHolder = (UserMessageViewHolder) viewHolder;
            userViewHolder.userMessageTextView.setText(chatMessage.getMessageText());
        } else if (viewHolder instanceof ChatbotMessageViewHolder) {
            ChatbotMessageViewHolder chatbotViewHolder = (ChatbotMessageViewHolder) viewHolder;
            chatbotViewHolder.chatbotMessageTextView.setText(chatMessage.getMessageText());
        }
    }


    // ViewHolder for user messages
    private static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageTextView;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            userMessageTextView = itemView.findViewById(R.id.userMessageTextView);
        }
    }

    // ViewHolder for chatbot messages
    private static class ChatbotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView chatbotMessageTextView;

        ChatbotMessageViewHolder(View itemView) {
            super(itemView);
            chatbotMessageTextView = itemView.findViewById(R.id.chatbotMessageTextView);
        }
    }
}

