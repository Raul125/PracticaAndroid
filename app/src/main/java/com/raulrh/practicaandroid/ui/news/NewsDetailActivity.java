package com.raulrh.practicaandroid.ui.news;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.ui.news.data.News;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NewsDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView categoriesTextView = findViewById(R.id.categoriesTextView);

        String newsJson = getIntent().getStringExtra("news");
        News news = new Gson().fromJson(newsJson, News.class);

        titleTextView.setText(news.title);
        dateTextView.setText(news.dateCreated);
        descriptionTextView.setText(Html.fromHtml(news.description));
        // categoriesTextView.setText();
    }
}
