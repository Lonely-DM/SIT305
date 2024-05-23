// CreateAdvertFragment.java

package com.example.task91p;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvertFragment extends Fragment {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private EditText nameEditText, phoneEditText, descriptionEditText, dateEditText, locationEditText;
    private RadioGroup postTypeRadioGroup;
    private Button saveButton, getLocationButton;
    private FusedLocationProviderClient fusedLocationClient;
    private DBHelper dbHelper;
    private MapsHelper mapsHelper;

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
        saveButton = view.findViewById(R.id.saveButton);
        getLocationButton = view.findViewById(R.id.getLocationButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getLocationButton.setOnClickListener(v -> checkLocationPermission());

        // Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.google_maps_key), Locale.US);
        }
        PlacesClient placesClient = Places.createClient(getContext());

        locationEditText.setOnClickListener(v -> startAutocompleteActivity());

        saveButton.setOnClickListener(v -> saveAdvert());

        return view;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeLocationServices();
        }
    }

    private void initializeLocationServices() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            updateLocationUI(location);
                        } else {
                            Log.w("CreateAdvertFragment", "Location is null. Ensure the emulator has a location set.");
                            Toast.makeText(getContext(), "Failed to get location. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CreateAdvertFragment", "Failed to get location: " + e.getMessage());
                        Toast.makeText(getContext(), "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateLocationUI(Location location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                locationEditText.setText(address.getAddressLine(0));
            } else {
                Toast.makeText(getContext(), "Unable to get address for the location.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Geocoder error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeLocationServices();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                locationEditText.setText(place.getAddress());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("CreateAdvertFragment", status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation
            }
        }
    }

    private void saveAdvert() {
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String location = locationEditText.getText().toString();
        int selectedId = postTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getView().findViewById(selectedId);
        String postType = selectedRadioButton.getText().toString();

        // Initialize DBHelper if not already done
        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext());
        }

        boolean insertData = dbHelper.insertAdvert(postType, name, phone, description, date, location);

        if (insertData) {
            Toast.makeText(getContext(), "Advert Saved Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}


// DBHelper.java

package com.example.task91p;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task91p.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ADVERTS = "adverts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_POST_TYPE = "post_type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ADVERTS_TABLE = "CREATE TABLE " + TABLE_ADVERTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_POST_TYPE + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_LOCATION + " TEXT)";
        db.execSQL(CREATE_ADVERTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVERTS);
        onCreate(db);
    }

    public boolean insertAdvert(String postType, String name, String phone, String description, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_TYPE, postType);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LOCATION, location);

        long result = db.insert(TABLE_ADVERTS, null, values);
        return result != -1;
    }

    public Cursor getAllAdverts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ADVERTS, null);
    }

    public ItemDetail getItemDetailById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADVERTS, new String[]{COLUMN_ID, COLUMN_POST_TYPE, COLUMN_NAME, COLUMN_PHONE, COLUMN_DESCRIPTION, COLUMN_DATE, COLUMN_LOCATION},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        ItemDetail itemDetail = new ItemDetail(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))
        );
        cursor.close();
        return itemDetail;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ADVERTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<ItemDetail> getAllItemDetails() {
        List<ItemDetail> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADVERTS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ItemDetail itemDetail = new ItemDetail(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POST_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))
                );
                itemList.add(itemDetail);
            }
            cursor.close();
        }
        return itemList;
    }
}


// DetailFragment.java

package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    private TextView typeTextView, itemTextView, descriptionTextView, dateTextView, locationTextView;
    private Button deleteButton;
    private DBHelper dbHelper;
    private int itemId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        typeTextView = view.findViewById(R.id.typeTextView);
        itemTextView = view.findViewById(R.id.itemTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        locationTextView = view.findViewById(R.id.locationTextView);
        deleteButton = view.findViewById(R.id.deleteButton);

        dbHelper = new DBHelper(getActivity());

        if (getArguments() != null) {
            itemId = getArguments().getInt("item_id");
            loadItemDetails(itemId);
        }

        deleteButton.setOnClickListener(v -> deleteItem(itemId));

        return view;
    }

    private void loadItemDetails(int id) {
        ItemDetail itemDetail = dbHelper.getItemDetailById(id);
        if (itemDetail != null) {
            typeTextView.setText(itemDetail.getPostType());
            itemTextView.setText(itemDetail.getName());
            descriptionTextView.setText(itemDetail.getDescription());
            dateTextView.setText(itemDetail.getDate());
            locationTextView.setText(itemDetail.getLocation());
        }
    }

    private void deleteItem(int id) {
        boolean deleted = dbHelper.deleteItem(id);
        if (deleted) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}


// HomeFragment.java

package com.example.task91p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button createAdvertButton = view.findViewById(R.id.createAdvertButton);
        Button showAllItemsButton = view.findViewById(R.id.showAllItemsButton);
        Button showOnMapButton = view.findViewById(R.id.showOnMapButton);

        createAdvertButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).navigateToFragment(new CreateAdvertFragment());
        });

        showAllItemsButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).navigateToFragment(new ListItemsFragment());
        });

        showOnMapButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).navigateToFragment(new MapFragment());
        });

        return view;
    }
}


// ItemDetail.java

package com.example.task91p;

import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ItemDetail {

    private int id;
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;

    public ItemDetail(int id, String postType, String name, String phone, String description, String date, String location) {
        this.id = id;
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getPostType() {
        return postType;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude(Geocoder geocoder) {
        return parseLatitudeOrLongitude(geocoder, 0);
    }

    public double getLongitude(Geocoder geocoder) {
        return parseLatitudeOrLongitude(geocoder, 1);
    }

    private double parseLatitudeOrLongitude(Geocoder geocoder, int index) {
        try {
            String[] parts = location.split(",");
            if (parts.length == 2) {
                return Double.parseDouble(parts[index].trim());
            } else {
                return geocodeLocation(geocoder, index);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0; // Default value or you can throw an exception
    }

    private double geocodeLocation(Geocoder geocoder, int index) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return index == 0 ? address.getLatitude() : address.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0; // Default value or you can throw an exception
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}


// ListItemsFragment.java

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


// MainActivity.java

package com.example.task91p;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}


// MapFragment.java

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


// MapsHelper.java

package com.example.task91p;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsHelper {

    private Context context;

    public MapsHelper(Context context) {
        this.context = context;
    }

    public String getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}


