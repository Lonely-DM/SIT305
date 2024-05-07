package com.example.task51csubtask1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class NewsDetailFragment extends Fragment {

    public static final String ARG_NEWS_ITEM = "newsItem";
    private NewsItem newsItem;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_news_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_NEWS_ITEM)) {
            newsItem = (NewsItem) getArguments().getSerializable(ARG_NEWS_ITEM);
        }

        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        ImageView image = view.findViewById(R.id.news_image);

        title.setText(newsItem.getTitle());
        description.setText(newsItem.getDescription());
        image.setImageResource(R.drawable.image_outline_filled);
    }
}

