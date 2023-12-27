package com.fidel.mindful;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    private List<VideoCall> videoCallList;

    public NotificationsAdapter(Context context, List<VideoCall> videoCallList) {
        this.context = context;
        this.videoCallList = videoCallList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoCall videoCall = videoCallList.get(position);

        // Customize how notification data is displayed in the list item
        holder.textViewFullName.setText("" + videoCall.getFullName()); // Display fullName
        holder.textViewName.setText("" + videoCall.getName());
        holder.textViewStartTime.setText("" + videoCall.getStartTime());
        holder.textViewStartDate.setText("" + videoCall.getStartDate());
        holder.textViewPassword.setText("" + videoCall.getSessionPassword());
        // Add more data binding as needed
        Picasso.get().load(videoCall.getImageUrl()).into(holder.imageProfile);

    }

    @Override
    public int getItemCount() {
        return videoCallList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFullName,textViewPassword, textViewName, textViewStartTime, textViewStartDate;
        ImageView imageProfile;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPassword = itemView.findViewById(R.id.textViewPassword);
            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewStartTime = itemView.findViewById(R.id.textViewStartTime);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            imageProfile = itemView.findViewById(R.id.imageProfile); // Corrected this line
            // Add more view bindings here
        }
    }
}
