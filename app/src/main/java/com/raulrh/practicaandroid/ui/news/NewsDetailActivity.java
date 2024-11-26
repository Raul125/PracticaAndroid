package com.raulrh.practicaandroid.ui.news;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.google.gson.Gson;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ActivityNewsDetailBinding;
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
    private ActivityNewsDetailBinding binding;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String newsJson = getIntent().getStringExtra("news");
        news = new Gson().fromJson(newsJson, News.class);

        binding.titleTextNews.setText(news.getTitle());
        binding.descriptionTextView.setText(HtmlCompat.fromHtml(news.getDescription()
                .replaceAll("<img.+/(img)*>", ""), HtmlCompat.FROM_HTML_MODE_LEGACY));

        configureDate();
        configureCategories();
        configureImages();
    }

    private void configureDate() {
        LocalDateTime localDateTime = LocalDateTime.parse(news.getDateCreated());
        LocalDate localDate = localDateTime.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.getDefault());
        String formattedDate = localDate.format(formatter);
        binding.dateTextNews.setText(formattedDate);
    }

    private void configureCategories() {
        TextView categoriesTextView = binding.categoriesTextNews;
        List<Category> categoriesList = news.getCategory();
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
    }

    private void configureImages() {
        List<Image> imageList = news.getImage();
        if (imageList != null && !imageList.isEmpty()) {
            for (Image image : imageList) {
                ImageView imageView = new ImageView(this);
                String imageUrl = "https://www.zaragoza.es/cont/paginas/noticias/" + image.getSrc();
                Picasso.get()
                        .load(imageUrl)
                        .resize(1000, 1000)
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .into(imageView);
                binding.newsDetailLayout.addView(imageView);
            }
        }
    }
}
