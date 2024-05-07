package com.example.task71p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnCreateAdvert = view.findViewById(R.id.btnCreateAdvert);
        Button btnViewItems = view.findViewById(R.id.btnViewItems);

        btnCreateAdvert.setOnClickListener(v -> navigateToFragment(new CreateAdvertFragment()));
        btnViewItems.setOnClickListener(v -> navigateToFragment(new ListItemsFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(fragment, true);
        }
    }
}
