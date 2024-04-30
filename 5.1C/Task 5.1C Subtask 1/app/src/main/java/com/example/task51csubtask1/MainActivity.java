package com.example.newsapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnNewsListener {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // This should be replaced with your actual data source
        newsAdapter = new NewsAdapter(NewsItem.getSampleNews(), this);
        recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void onNewsClick(int position) {
        NewsItem newsItem = newsAdapter.getNewsItemAt(position);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, NewsDetailFragment.newInstance(newsItem))
                .addToBackStack(null)
                .commit();
    }
}
