package com.example.task91p;

import android.location.Geocoder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBHelper dbHelper;
    private Geocoder geocoder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        dbHelper = new DBHelper(getActivity());
        geocoder = new Geocoder(getActivity());
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        List<ItemDetail> itemDetails = dbHelper.getAllItemDetails();
        for (ItemDetail item : itemDetails) {
            double latitude = item.getLatitude(geocoder);
            double longitude = item.getLongitude(geocoder);
            if (latitude != 0.0 && longitude != 0.0) {
                LatLng location = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(item.getName()));
            }
        }

        // Move the camera to the first item
        if (!itemDetails.isEmpty()) {
            ItemDetail firstItem = itemDetails.get(0);
            LatLng firstLocation = new LatLng(firstItem.getLatitude(geocoder), firstItem.getLongitude(geocoder));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
        }
    }
}
