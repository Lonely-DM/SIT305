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
