package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button createAdvertButton = view.findViewById(R.id.createAdvertButton);
        Button showAllItemsButton = view.findViewById(R.id.showAllItemsButton);
        Button showOnMapButton = view.findViewById(R.id.showOnMapButton);

        createAdvertButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new CreateAdvertFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        showAllItemsButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ListItemsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        showOnMapButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MapFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
