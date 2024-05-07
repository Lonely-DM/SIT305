package com.example.task51csubtask1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        NewsItem newsItem = getIntent().getParcelableExtra("newsItem");

        ImageView imageView = findViewById(R.id.detailImage);
        TextView titleView = findViewById(R.id.detailTitle);
        TextView descriptionView = findViewById(R.id.detailDescription);

        imageView.setImageResource(newsItem.getImageResourceId());
        titleView.setText(newsItem.getTitle());
        descriptionView.setText(newsItem.getDescription());
    }

}

