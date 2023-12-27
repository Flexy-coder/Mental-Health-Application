package com.fidel.mindful.ui.userlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fidel.mindful.R;
import com.fidel.mindful.ui.messaging.MessagingFragment;

public class UserListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlist, container, false);

        ListView userListView = view.findViewById(R.id.userListView);

        // Dummy data for usernames and emails
        String[] userInfos = {
                "User1 (user1@example.com)",
                "User2 (user2@example.com)",
                "User3 (user3@example.com)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, userInfos);

        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When an item is clicked, redirect to the messaging fragment
                MessagingFragment messagingFragment = new MessagingFragment();

                // You can pass the selected user info to the messaging fragment if needed
                Bundle args = new Bundle();
                args.putString("userInfo", userInfos[position]);
                messagingFragment.setArguments(args);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, messagingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
