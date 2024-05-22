package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    private DBHelper dbHelper;
    private int itemId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView typeTextView = view.findViewById(R.id.typeTextView);
        TextView itemTextView = view.findViewById(R.id.itemTextView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        TextView locationTextView = view.findViewById(R.id.locationTextView);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        dbHelper = new DBHelper(getActivity());
        if (getArguments() != null) {
            itemId = getArguments().getInt("itemId");
            ItemDetail itemDetail = dbHelper.getItemDetailById(itemId);

            if (itemDetail != null) {
                typeTextView.setText(itemDetail.getType());
                itemTextView.setText(itemDetail.getItem());
                descriptionTextView.setText(itemDetail.getDescription());
                dateTextView.setText(itemDetail.getDate());
                locationTextView.setText(itemDetail.getLocation());
            }
        }

        deleteButton.setOnClickListener(v -> {
            dbHelper.deleteItem(itemId);
            Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
