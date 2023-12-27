package com.fidel.mindful.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fidel.mindful.BreathingActivity;
import com.fidel.mindful.CareActivity;
import com.fidel.mindful.ChatbotActivity;
import com.fidel.mindful.EasyChatActivity;
import com.fidel.mindful.FeedbackdataActivity;
import com.fidel.mindful.ImageAdapter;
import com.fidel.mindful.ImageAdapter2;
import com.fidel.mindful.ImageItem;
import com.fidel.mindful.MusicActivity;
import com.fidel.mindful.R;
import com.fidel.mindful.TherapistAdapter;
import com.fidel.mindful.TherapistData;
import com.fidel.mindful.TherapistListing2;
import com.fidel.mindful.TherapistListing3;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewTherapists;
    private DatabaseReference therapistDatabaseReference;

    private TherapistAdapter therapistAdapter;
    private List<TherapistData> therapistProfiles;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        therapistProfiles = new ArrayList<>();
        // Initialize Firebase Database reference
        therapistDatabaseReference = FirebaseDatabase.getInstance().getReference().child("therapists");


        ImageView chatImageView = view.findViewById(R.id.chatImageView);
        chatImageView.setOnClickListener(v -> startActivity(new Intent(getActivity(), EasyChatActivity.class)));
        ImageView feedbackImageView = view.findViewById(R.id.feedbackImageView);
        feedbackImageView.setOnClickListener(v -> startActivity(new Intent(getActivity(), FeedbackdataActivity.class)));

        RecyclerView imageRecyclerView = view.findViewById(R.id.imageRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);

        List<ImageItem> imageList = new ArrayList<>();
        imageList.add(new ImageItem(R.drawable.bookings, "Book a counselling session"));
        imageList.add(new ImageItem(R.drawable.reminder, "List of bookings"));
        imageList.add(new ImageItem(R.drawable.chat, "Message a therapist"));
        imageList.add(new ImageItem(R.drawable.journal, "Put your thoughts in writing"));
        imageList.add(new ImageItem(R.drawable.community, "Find your Community"));
        ImageAdapter imageAdapter = new ImageAdapter(imageList);
        imageRecyclerView.setAdapter(imageAdapter);

//        RecyclerView imageRecyclerView2 = view.findViewById(R.id.imageRecyclerView2);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
//        imageRecyclerView2.setLayoutManager(layoutManager2);

//        List<Integer> imageResourceIds2 = new ArrayList<>();
//        imageResourceIds2.add(R.drawable.breathe2);
//        imageResourceIds2.add(R.drawable.music2);
//        imageResourceIds2.add(R.drawable.phone);
//        imageResourceIds2.add(R.drawable.med);

//        List<String> imageUrls2 = new ArrayList<>();
//        for (int resourceId : imageResourceIds2) {
//            imageUrls2.add("android.resource://" + requireContext().getPackageName() + "/" + resourceId);
//        }

//        ImageAdapter2 imageAdapter2 = new ImageAdapter2(imageUrls2, requireContext());
//        imageRecyclerView2.setAdapter(imageAdapter2);
//
//        // Set item click listener for the RecyclerView
//        imageAdapter2.setOnItemClickListener(new ImageAdapter2.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                // Handle click event based on the position
//                switch (position) {
//                    case 0:
//                        // Handle the "breathe" action
//                        startActivity(new Intent(getActivity(), BreathingActivity.class));
//                        break;
//                    case 1:
//                        // Handle the "music" action
//                        startActivity(new Intent(getActivity(), MusicActivity.class));
//                        break;
//                    case 2:
//                        // Handle the "affirmations" action
//                        startActivity(new Intent(getActivity(), TherapistListing2.class));
//                        break;
//                    case 3:
//                        // Handle the "care" action
//                        startActivity(new Intent(getActivity(), CareActivity.class));
//                        break;
//                    default:
//                        // Handle other positions, if necessary
//                        break;
//                }
//            }
//        });

        recyclerViewTherapists = view.findViewById(R.id.recyclerViewTherapists);
        therapistAdapter = new TherapistAdapter(new ArrayList<>());
        recyclerViewTherapists.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTherapists.setAdapter(therapistAdapter);

        // Load therapist profiles using AsyncTask
        new LoadTherapistProfilesTask().execute();
        // Find the TextView by its ID
        TextView moreTextView = view.findViewById(R.id.moreTextView);

        // Set OnClickListener
        moreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start TherapistListing2
                Intent intent = new Intent(requireContext(), TherapistListing3.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void refreshData() {
        new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            // Update your UI or reload data as needed
            // You can call loadTherapistProfilesInBackground() or any other data refresh logic here
        }, 2000);
    }

    private class LoadTherapistProfilesTask extends AsyncTask<Void, Void, List<TherapistData>> {
        @Override
        protected List<TherapistData> doInBackground(Void... voids) {
            // Fetch therapist profiles from Firebase Database
            List<TherapistData> therapistProfiles = new ArrayList<>();

            therapistDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    therapistProfiles.clear(); // Clear the list to avoid duplicates

                    for (DataSnapshot therapistSnapshot : dataSnapshot.getChildren()) {
                        String imageUrl = therapistSnapshot.child("imageUrl").getValue(String.class);
                        String name = therapistSnapshot.child("fullName").getValue(String.class);
                        String description = therapistSnapshot.child("description").getValue(String.class);

                        // Create a TherapistData object and add it to the list
                        therapistProfiles.add(new TherapistData(imageUrl, name, description));
                    }

                    therapistAdapter.updateData(therapistProfiles);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that may occur during data retrieval
                }
            });

            return therapistProfiles;
        }

        @Override
        protected void onPostExecute(List<TherapistData> therapistProfiles) {
            super.onPostExecute(therapistProfiles);
            // The therapistAdapter.updateData(therapistProfiles) is now called within onDataChange()
            // No need to update it here again.
        }
    }
}
