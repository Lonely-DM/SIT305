package com.example.task91p;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

public class MapsHelper {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public MapsHelper(Context context) {
        this.context = context;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (!Places.isInitialized()) {
            Places.initialize(context.getApplicationContext(), context.getString(R.string.google_maps_key));
        }
    }

    public void fetchCurrentLocation(OnCompleteListener<Location> listener) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(listener);
        }
    }

    public void openAutocomplete(Context context, int requestCode) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(context);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public void initializeMap(SupportMapFragment mapFragment, OnMapReadyCallback callback) {
        mapFragment.getMapAsync(callback);
    }

    public void addMarker(GoogleMap googleMap, LatLng latLng, String title) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
    }
}
