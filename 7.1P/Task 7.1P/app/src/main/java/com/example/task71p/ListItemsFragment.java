package com.example.task71p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import java.util.List;

public class ListItemsFragment extends Fragment {

    private DBHelper dbHelper;

    public ListItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_items, container, false);
        ListView lvItems = view.findViewById(R.id.lvItems);
        dbHelper = new DBHelper(getContext());

        List<ItemDetail> items = dbHelper.getAllItemDetailsWithId();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                items.stream().map(ItemDetail::toString).toArray(String[]::new));
        lvItems.setAdapter(adapter);

        lvItems.setOnItemClickListener((parent, view1, position, id) -> {
            int itemId = items.get(position).getId();
            navigateToDetailFragment(itemId);
        });

        return view;
    }

    private void navigateToDetailFragment(int itemId) {
        if (getActivity() instanceof MainActivity) {
            DetailFragment detailFragment = new DetailFragment(itemId);
            ((MainActivity) getActivity()).showFragment(detailFragment, true);
        }
    }


}
