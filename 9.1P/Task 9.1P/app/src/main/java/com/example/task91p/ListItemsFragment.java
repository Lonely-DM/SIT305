package com.example.task91p;

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
import java.util.List;

public class ListItemsFragment extends Fragment {

    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_items, container, false);
        ListView listView = view.findViewById(R.id.listView);

        dbHelper = new DBHelper(getActivity());
        List<ItemDetail> items = dbHelper.getAllItemDetails();
        ArrayAdapter<ItemDetail> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View v, int position, long id) -> {
            ItemDetail item = items.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt("itemId", item.getId());

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
