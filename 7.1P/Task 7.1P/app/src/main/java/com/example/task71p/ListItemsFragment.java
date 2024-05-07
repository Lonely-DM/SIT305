package com.example.task71p;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ListItemsFragment extends Fragment {

    private DBHelper dbHelper;

    public ListItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_items, container, false);
        dbHelper = new DBHelper(getContext());
        ListView lvItems = view.findViewById(R.id.lvItems);

        ArrayList<String> itemDetails = new ArrayList<>();
        Cursor cursor = dbHelper.getAllItems();

        while (cursor.moveToNext()) {
            itemDetails.add(cursor.getString(cursor.getColumnIndexOrThrow("type")) + " - " +
                    cursor.getString(cursor.getColumnIndexOrThrow("item")));
        }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, itemDetails);
        lvItems.setAdapter(itemsAdapter);

        return view;
    }
}
