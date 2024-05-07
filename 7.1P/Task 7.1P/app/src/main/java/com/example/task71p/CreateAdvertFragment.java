package com.example.task71p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class CreateAdvertFragment extends Fragment {

    private DBHelper dbHelper;

    public CreateAdvertFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_advert, container, false);
        dbHelper = new DBHelper(getContext());

        Spinner spinnerType = view.findViewById(R.id.spinnerLostFound);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.lost_found_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        EditText etItem = view.findViewById(R.id.etItem);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etLocation = view.findViewById(R.id.etLocation);
        Button btnPost = view.findViewById(R.id.btnPost);

        btnPost.setOnClickListener(v -> {
            String type = spinnerType.getSelectedItem().toString();
            String item = etItem.getText().toString();
            String phone = etPhone.getText().toString();
            String description = etDescription.getText().toString();
            String date = etDate.getText().toString();
            String location = etLocation.getText().toString();

            // Add item to the database
            dbHelper.addItem(type, item, phone, description, date, location);
            Toast.makeText(getContext(), "Item posted successfully!", Toast.LENGTH_SHORT).show();

            // Transition back to the HomeFragment
            ((MainActivity)getActivity()).showFragment(new HomeFragment());
        });

        return view;
    }
}
