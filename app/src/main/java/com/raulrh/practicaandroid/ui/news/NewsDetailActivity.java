package com.raulrh.practicaandroid.ui.news;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.google.gson.Gson;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.ui.news.data.Category;
import com.raulrh.practicaandroid.ui.news.data.Image;
import com.raulrh.practicaandroid.ui.news.data.News;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

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

        titleTextView.setText(news.getTitle());

        LocalDateTime localDateTime = LocalDateTime.parse(news.getDateCreated());
        LocalDate localDate = localDateTime.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.getDefault());

        String formattedDate = localDate.format(formatter);
        dateTextView.setText(formattedDate);

        descriptionTextView.setText(HtmlCompat.fromHtml(news.getDescription().replaceAll("<img.+/(img)*>", ""), HtmlCompat.FROM_HTML_MODE_LEGACY));

        TextView categoriesTextView = findViewById(R.id.categoriesTextNews);
        List<Category> categoriesList = news.getCategories();
        if (categoriesList != null && !categoriesList.isEmpty()) {
            StringBuilder categories = new StringBuilder();
            int size = categoriesList.size();
            for (int i = 0; i < size; i++) {
                categories.append(categoriesList.get(i).getTitle());
                if (i < size - 1) {
                    categories.append("\n");
                }
            }

            categoriesTextView.setText(categories.toString());
        }

        List<Image> imageList = news.getImages();
        if (imageList != null && !imageList.isEmpty()) {
            LinearLayout container = findViewById(R.id.newsDetailLayout);
            for (Image image : imageList) {
                ImageView imageView = new ImageView(this);
                String imageUrl = "https://www.zaragoza.es/cont/paginas/noticias/" + image.getSrc();
                Picasso.get()
                        .load(imageUrl)
                        .resize(1000, 1000)
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .into(imageView);

                container.addView(imageView);
            }
        }
    }
}
