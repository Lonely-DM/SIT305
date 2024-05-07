package com.example.task51csubtask1;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Parcelable {
    private int imageResourceId;
    private String title;
    private String description;

    public NewsItem(int imageResourceId, String title, String description) {
        this.imageResourceId = imageResourceId;
        this.title = title;
        this.description = description;
    }

    protected NewsItem(Parcel in) {
        imageResourceId = in.readInt();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(imageResourceId);
        parcel.writeString(title);
        parcel.writeString(description);
    }

    // Getter methods
    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}


