//MainActivity.java

package com.example.task71p;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            showFragment(new HomeFragment(), false);  // Load HomeFragment initially without backstack management
        }
    }

    public void showFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}

//CreateAdvertFragment.java
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

//DBHelper.java

package com.example.task71p;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LostFoundDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ITEM = "item";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LOCATION = "location";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_ITEM + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_LOCATION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public void addItem(String type, String item, String description, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_ITEM, item);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LOCATION, location);
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    public List<ItemDetail> getAllItemDetailsWithId() {
        List<ItemDetail> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_ITEM, COLUMN_DESCRIPTION, COLUMN_DATE, COLUMN_LOCATION}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String item = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
                items.add(new ItemDetail(id, type, item, description, date, location));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public ItemDetail getItemDetailsById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_ITEM, COLUMN_DESCRIPTION, COLUMN_DATE, COLUMN_LOCATION}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        ItemDetail itemDetail = null;
        if (cursor.moveToFirst()) {
            String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            String item = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
            itemDetail = new ItemDetail(id, type, item, description, date, location);
        }
        cursor.close();
        db.close();
        return itemDetail;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}

//DetailFragment.java

package com.example.task71p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.widget.Button;

public class DetailFragment extends Fragment {
    private int itemId;  // ID of the item to display and remove

    public DetailFragment(int itemId) {
        this.itemId = itemId;  // Constructor to initialize the item ID
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView tvDetails = view.findViewById(R.id.tvDetails);
        Button btnRemove = view.findViewById(R.id.btnRemove);

        DBHelper dbHelper = new DBHelper(getContext());
        ItemDetail itemDetail = dbHelper.getItemDetailsById(itemId);

        if (itemDetail != null) {
            String details = "Type: " + itemDetail.getType() + "\nItem: " + itemDetail.getItem() +
                    "\nDescription: " + itemDetail.getDescription() + "\nDate: " +
                    itemDetail.getDate() + "\nLocation: " + itemDetail.getLocation();
            tvDetails.setText(details);
        } else {
            tvDetails.setText("No details found.");
        }

        btnRemove.setOnClickListener(v -> {
            dbHelper.deleteItem(itemId);  // Call DBHelper to remove the item
            getActivity().getSupportFragmentManager().popBackStack();  // Go back to the previous fragment
        });

        return view;
    }
}

//HomeFragment.java
package com.example.task71p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnCreateAdvert = view.findViewById(R.id.btnCreateAdvert);
        Button btnViewItems = view.findViewById(R.id.btnViewItems);

        btnCreateAdvert.setOnClickListener(v -> navigateToFragment(new CreateAdvertFragment()));
        btnViewItems.setOnClickListener(v -> navigateToFragment(new ListItemsFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showFragment(fragment, true);
        }
    }
}

//ItemDetail.java

package com.example.task71p;

public class ItemDetail {
    private int id;
    private String type;
    private String item;
    private String description;
    private String date;
    private String location;

    public ItemDetail(int id, String type, String item, String description, String date, String location) {
        this.id = id;
        this.type = type;
        this.item = item;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getItem() {
        return item;
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

    @Override
    public String toString() {
        return type + " - " + item;
    }
}

//ListItemsFragment.java

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
