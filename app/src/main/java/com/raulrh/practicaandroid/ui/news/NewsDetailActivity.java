package com.raulrh.practicaandroid.ui.news;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.ui.news.data.Category;
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

        String newsJson = getIntent().getStringExtra("news");
        News news = new Gson().fromJson(newsJson, News.class);

        titleTextView.setText(news.title);
        dateTextView.setText(news.dateCreated);
        descriptionTextView.setText(Html.fromHtml(news.description));

        TextView categoriesTextView = findViewById(R.id.categoriesTextView);
        if (news.category != null && !news.category.isEmpty()) {
            StringBuilder categories = new StringBuilder();
            int size = news.category.size();

            for (int i = 0; i < size; i++) {
                categories.append(news.category.get(i).title);
                if (i < size - 1) {
                    categories.append("\n");
                }
            }

            categoriesTextView.setText(categories.toString());
        }
    }
}
