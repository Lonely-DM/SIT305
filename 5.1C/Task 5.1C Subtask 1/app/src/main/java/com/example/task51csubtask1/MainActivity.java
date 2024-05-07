package com.example.task51csubtask1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView topStoriesRecyclerView;
    private RecyclerView newsRecyclerView;
    private NewsAdapter topStoriesAdapter;
    private NewsAdapter newsAdapter;
    private List<NewsItem> topStoriesList;
    private List<NewsItem> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topStoriesRecyclerView = findViewById(R.id.topStoriesRecyclerView);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);

        // Initialize data
        topStoriesList = createSampleData();
        newsList = createSampleData().subList(0, 4);  // Ensure there are at least 4 items

        // Setup the adapters with click listeners
        topStoriesAdapter = new NewsAdapter(topStoriesList, this::openNewsDetail);
        newsAdapter = new NewsAdapter(newsList, this::openNewsDetail);

        // Set up RecyclerViews
        topStoriesRecyclerView.setAdapter(topStoriesAdapter);
        topStoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        newsRecyclerView.setLayoutManager(gridLayoutManager);
        newsRecyclerView.setAdapter(newsAdapter);
    }

    private void openNewsDetail(NewsItem item) {
        Intent intent = new Intent(MainActivity.this, NewsDetailsActivity.class);
        intent.putExtra("newsItem", item);
        startActivity(intent);
    }

    private List<NewsItem> createSampleData() {
        List<NewsItem> items = new ArrayList<>();
        items.add(new NewsItem(R.drawable.image_outline_filled, getString(R.string.news_title_1), getString(R.string.news_description_1)));
        items.add(new NewsItem(R.drawable.image_outline_filled, getString(R.string.news_title_2), getString(R.string.news_description_2)));
        items.add(new NewsItem(R.drawable.image_outline_filled, getString(R.string.news_title_3), getString(R.string.news_description_3)));
        items.add(new NewsItem(R.drawable.image_outline_filled, getString(R.string.news_title_4), getString(R.string.news_description_4)));
        items.add(new NewsItem(R.drawable.image_outline_filled, getString(R.string.news_title_5), getString(R.string.news_description_5)));
        items.add(new NewsItem(R.drawable.image_outline_filled, getString(R.string.news_title_6), getString(R.string.news_description_6)));
        return items;
    }
}


