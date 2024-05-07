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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_advert, container, false);
        dbHelper = new DBHelper(getContext());

        Spinner spinnerType = view.findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.type_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        EditText etItemName = view.findViewById(R.id.etItemName);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etLocation = view.findViewById(R.id.etLocation);
        Button btnPostAdvert = view.findViewById(R.id.btnPostAdvert);

        btnPostAdvert.setOnClickListener(v -> {
            String type = spinnerType.getSelectedItem().toString();
            String itemName = etItemName.getText().toString();
            String description = etDescription.getText().toString();
            String date = etDate.getText().toString();
            String location = etLocation.getText().toString();

            dbHelper.addItem(type, itemName, description, date, location);
            Toast.makeText(getContext(), "Advert Posted Successfully!", Toast.LENGTH_LONG).show();

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showFragment(new HomeFragment(), false);
            }
        });

        return view;
    }
}
