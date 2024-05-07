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
