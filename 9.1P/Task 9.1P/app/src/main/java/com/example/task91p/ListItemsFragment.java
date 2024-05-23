package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class ListItemsFragment extends Fragment {

    private ListView listView;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_items, container, false);

        listView = view.findViewById(R.id.listView);
        dbHelper = new DBHelper(getActivity());

        loadItems();

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            ItemDetail itemDetail = (ItemDetail) parent.getItemAtPosition(position);
            Bundle bundle = new Bundle();
            bundle.putInt("item_id", itemDetail.getId());
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);
            ((MainActivity) getActivity()).navigateToFragment(detailFragment);
        });

        return view;
    }

    private void loadItems() {
        List<ItemDetail> itemList = dbHelper.getAllItemDetails();
        ArrayAdapter<ItemDetail> adapter = new ArrayAdapter<ItemDetail>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, itemList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                ItemDetail itemDetail = getItem(position);
                if (itemDetail != null) {
                    text1.setText(itemDetail.getPostType() + ": " + itemDetail.getName());
                    text2.setText(itemDetail.getDescription());
                }
                return view;
            }
        };
        listView.setAdapter(adapter);
    }
}
