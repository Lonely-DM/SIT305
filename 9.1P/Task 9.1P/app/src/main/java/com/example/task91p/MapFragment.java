package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapsHelper mapsHelper;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapsHelper = new MapsHelper(getActivity());
        dbHelper = new DBHelper(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<ItemDetail> items = dbHelper.getAllItemDetails();
        for (ItemDetail item : items) {
            LatLng latLng = new LatLng(Double.parseDouble(item.getLocation().split(",")[0]), Double.parseDouble(item.getLocation().split(",")[1]));
            mapsHelper.addMarker(googleMap, latLng, item.getItem());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.8688, 151.2093), 10));
    }
}
