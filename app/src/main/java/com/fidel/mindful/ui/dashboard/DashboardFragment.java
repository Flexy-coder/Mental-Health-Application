package com.fidel.mindful.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fidel.mindful.ImageAdapter;
import com.fidel.mindful.ImageAdapter2;
import com.fidel.mindful.ImageItem;
import com.fidel.mindful.R;
import com.fidel.mindful.databinding.FragmentDashboardBinding;


import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set click listeners for image views
        ImageView anxietyImageView = root.findViewById(R.id.anxiety);
        ImageView depressionImageView = root.findViewById(R.id.depression);
        ImageView drugsImageView = root.findViewById(R.id.drugs);
        ImageView ptsdImageView = root.findViewById(R.id.ptsd);



        // Set up RecyclerView for images
        RecyclerView imageRecyclerView = root.findViewById(R.id.imageRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);

        List<ImageItem> imageList = new ArrayList<>();
        imageList.add(new ImageItem(R.drawable.book, "Book a counselling session"));
        imageList.add(new ImageItem(R.drawable.call, "Contact a therapist"));
        imageList.add(new ImageItem(R.drawable.journal, "Put your thoughts in writing"));
        imageList.add(new ImageItem(R.drawable.community, "Find your Community"));

        ImageAdapter imageAdapter = new ImageAdapter(imageList);
        imageRecyclerView.setAdapter(imageAdapter);

        // Set up the second RecyclerView in this fragment
        RecyclerView imageRecyclerView2 = root.findViewById(R.id.imageRecyclerView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        imageRecyclerView2.setLayoutManager(layoutManager2);

        List<Integer> imageResourceIds2 = new ArrayList<>();
        imageResourceIds2.add(R.drawable.breathe);
        imageResourceIds2.add(R.drawable.music);
        imageResourceIds2.add(R.drawable.affirmations);
        imageResourceIds2.add(R.drawable.care);

        List<String> imageUrls2 = new ArrayList<>();
        for (int resourceId : imageResourceIds2) {
            imageUrls2.add("android.resource://" + requireContext().getPackageName() + "/" + resourceId);
        }

        ImageAdapter2 imageAdapter2 = new ImageAdapter2(imageUrls2, requireContext());
        imageRecyclerView2.setAdapter(imageAdapter2);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
