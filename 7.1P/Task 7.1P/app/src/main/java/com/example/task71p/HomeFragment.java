package com.example.task71p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnCreateAdvert = view.findViewById(R.id.btnCreateAdvert);
        Button btnViewItems = view.findViewById(R.id.btnViewItems);

        btnCreateAdvert.setOnClickListener(v -> ((MainActivity)getActivity()).showFragment(new CreateAdvertFragment()));
        btnViewItems.setOnClickListener(v -> ((MainActivity)getActivity()).showFragment(new ListItemsFragment()));

        return view;
    }
}
