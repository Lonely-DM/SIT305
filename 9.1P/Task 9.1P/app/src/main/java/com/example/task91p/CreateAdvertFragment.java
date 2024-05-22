package com.example.task91p;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

public class CreateAdvertFragment extends Fragment {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private EditText locationEditText;
    private EditText nameEditText, phoneEditText, descriptionEditText, dateEditText;
    private RadioGroup postTypeRadioGroup;
    private MapsHelper mapsHelper;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_advert, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        postTypeRadioGroup = view.findViewById(R.id.postTypeRadioGroup);

        Button getLocationButton = view.findViewById(R.id.getLocationButton);
        Button searchLocationButton = view.findViewById(R.id.searchLocationButton);
        Button saveButton = view.findViewById(R.id.saveButton);

        mapsHelper = new MapsHelper(getActivity());
        dbHelper = new DBHelper(getActivity());

        searchLocationButton.setOnClickListener(v -> mapsHelper.openAutocomplete(getActivity(), AUTOCOMPLETE_REQUEST_CODE));

        getLocationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                fetchCurrentLocation();
            }
        });

        saveButton.setOnClickListener(v -> saveAdvert());

        return view;
    }

    private void fetchCurrentLocation() {
        mapsHelper.fetchCurrentLocation(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    locationEditText.setText(latLng.latitude + "," + latLng.longitude);
                } else {
                    Toast.makeText(getActivity(), "Unable to fetch current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                locationEditText.setText(latLng.latitude + "," + latLng.longitude);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(getActivity(), "Error in retrieving location", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText(getActivity(), "Operation canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            }
        }
    }

    private void saveAdvert() {
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String location = locationEditText.getText().toString();
        int selectedPostTypeId = postTypeRadioGroup.getCheckedRadioButtonId();
        String postType = selectedPostTypeId == R.id.lostRadioButton ? "Lost" : "Found";

        dbHelper.addItem(postType, name, description, date, location);
        Toast.makeText(getActivity(), "Advert saved", Toast.LENGTH_SHORT).show();
    }
}
