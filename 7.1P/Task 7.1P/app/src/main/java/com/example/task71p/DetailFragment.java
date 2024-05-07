package com.example.task71p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.widget.Button;

public class DetailFragment extends Fragment {
    private int itemId;  // ID of the item to display and remove

    public DetailFragment(int itemId) {
        this.itemId = itemId;  // Constructor to initialize the item ID
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView tvDetails = view.findViewById(R.id.tvDetails);
        Button btnRemove = view.findViewById(R.id.btnRemove);

        DBHelper dbHelper = new DBHelper(getContext());
        ItemDetail itemDetail = dbHelper.getItemDetailsById(itemId);

        if (itemDetail != null) {
            String details = "Type: " + itemDetail.getType() + "\nItem: " + itemDetail.getItem() +
                    "\nDescription: " + itemDetail.getDescription() + "\nDate: " +
                    itemDetail.getDate() + "\nLocation: " + itemDetail.getLocation();
            tvDetails.setText(details);
        } else {
            tvDetails.setText("No details found.");
        }

        btnRemove.setOnClickListener(v -> {
            dbHelper.deleteItem(itemId);  // Call DBHelper to remove the item
            getActivity().getSupportFragmentManager().popBackStack();  // Go back to the previous fragment
        });

        return view;
    }
}

