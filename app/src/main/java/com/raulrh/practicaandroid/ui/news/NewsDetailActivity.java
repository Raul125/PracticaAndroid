package com.raulrh.practicaandroid.ui.news;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.google.gson.Gson;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.ui.news.data.News;

public class NewsDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        TextView titleTextView = findViewById(R.id.titleTextNews);
        TextView dateTextView = findViewById(R.id.dateTextNews);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        String newsJson = getIntent().getStringExtra("news");
        News news = new Gson().fromJson(newsJson, News.class);

        titleTextView.setText(news.title);
        dateTextView.setText(news.dateCreated);
        descriptionTextView.setText(HtmlCompat.fromHtml(news.description.replaceAll("<img.+/(img)*>", ""), HtmlCompat.FROM_HTML_MODE_LEGACY));

        TextView categoriesTextView = findViewById(R.id.categoriesTextNews);
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
