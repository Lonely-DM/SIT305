package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button createAdvertButton = view.findViewById(R.id.createAdvertButton);
        Button showAllItemsButton = view.findViewById(R.id.showAllItemsButton);
        Button showOnMapButton = view.findViewById(R.id.showOnMapButton);

        createAdvertButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).navigateToFragment(new CreateAdvertFragment());
        });

        showAllItemsButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).navigateToFragment(new ListItemsFragment());
        });

        showOnMapButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).navigateToFragment(new MapFragment());
        });

        return view;
    }
}
