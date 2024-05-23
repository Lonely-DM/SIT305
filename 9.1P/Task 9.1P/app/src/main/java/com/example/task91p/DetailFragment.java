package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    private TextView typeTextView, itemTextView, descriptionTextView, dateTextView, locationTextView;
    private Button deleteButton;
    private DBHelper dbHelper;
    private int itemId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        typeTextView = view.findViewById(R.id.typeTextView);
        itemTextView = view.findViewById(R.id.itemTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        locationTextView = view.findViewById(R.id.locationTextView);
        deleteButton = view.findViewById(R.id.deleteButton);

        dbHelper = new DBHelper(getActivity());

        if (getArguments() != null) {
            itemId = getArguments().getInt("item_id");
            loadItemDetails(itemId);
        }

        deleteButton.setOnClickListener(v -> deleteItem(itemId));

        return view;
    }

    private void loadItemDetails(int id) {
        ItemDetail itemDetail = dbHelper.getItemDetailById(id);
        if (itemDetail != null) {
            typeTextView.setText(itemDetail.getPostType());
            itemTextView.setText(itemDetail.getName());
            descriptionTextView.setText(itemDetail.getDescription());
            dateTextView.setText(itemDetail.getDate());
            locationTextView.setText(itemDetail.getLocation());
        }
    }

    private void deleteItem(int id) {
        boolean deleted = dbHelper.deleteItem(id);
        if (deleted) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
