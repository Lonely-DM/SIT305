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
