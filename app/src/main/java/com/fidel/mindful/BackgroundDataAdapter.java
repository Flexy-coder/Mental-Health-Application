package com.fidel.mindful;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class BackgroundDataAdapter extends RecyclerView.Adapter<BackgroundDataAdapter.ViewHolder> {
    private List<BackgroundDataModel> dataList;

    // Constructor to initialize the list
    public BackgroundDataAdapter(List<BackgroundDataModel> dataList) {
        this.dataList = dataList;
    }

    // ViewHolder class for the adapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dialogUserId;
        TextView dialogAge;
        TextView dialogGender;
        TextView dialogBirthOrder;
        TextView dialogMedicalHistory;
        TextView dialogFriendDescription;
        TextView dialogFamilyStructure;
        TextView dialogAlcoholConsumption;
        TextView dialogSocialInteractions;

        public ViewHolder(View itemView) {
            super(itemView);
            dialogUserId = itemView.findViewById(R.id.dialogUserId);
            dialogAge = itemView.findViewById(R.id.dialogAge);
            dialogGender = itemView.findViewById(R.id.dialogGender);
            dialogBirthOrder = itemView.findViewById(R.id.dialogBirthOrder);
            dialogMedicalHistory = itemView.findViewById(R.id.dialogMedicalHistory);
            dialogFriendDescription = itemView.findViewById(R.id.dialogFriendDescription);
            dialogFamilyStructure = itemView.findViewById(R.id.dialogFamilyStructure);
            dialogAlcoholConsumption = itemView.findViewById(R.id.dialogAlcoholConsumption);
            dialogSocialInteractions = itemView.findViewById(R.id.dialogSocialInteractions);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_background_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BackgroundDataModel data = dataList.get(position);
        // Set your data here based on your model using appropriate getters
        holder.dialogUserId.setText("Name: " +data.getFullName());
        holder.dialogBirthOrder.setText("Age: " + data.getAge());
        holder.dialogGender.setText("Gender: " + data.getGender());
        holder.dialogAge.setText("Birth Order: " +data.getBirthOrder());
        holder.dialogMedicalHistory.setText("Mental health medical history: " +data.getMedicalHistory());
        holder.dialogFriendDescription.setText("Friend description: " +data.getFriendDescription());
        holder.dialogFamilyStructure.setText("Family structure: " +data.getFamilyStructure());
        holder.dialogAlcoholConsumption.setText("Alcohol intake: " +data.getAlcoholConsumption());
        holder.dialogSocialInteractions.setText("Personality: " +data.getSocialInteractions());


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    // Method to update the adapter's dataset

}
