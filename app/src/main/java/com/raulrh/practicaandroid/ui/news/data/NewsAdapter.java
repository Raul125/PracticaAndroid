package com.raulrh.practicaandroid.ui.news.data;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ItemNewsBinding;
import com.raulrh.practicaandroid.ui.news.NewsDetailActivity;
import com.raulrh.practicaandroid.ui.news.NewsFragment;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private final List<News> newsList;
    private final NewsFragment newsFragment;

    public NewsAdapter(List<News> newsList, NewsFragment newsFragment) {
        this.newsList = newsList;
        this.newsFragment = newsFragment;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(newsFragment.getContext(), NewsDetailActivity.class);
            intent.putExtra("news", new Gson().toJson(news));
            newsFragment.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateList(List<News> newItems) {
        newsList.clear();
        newsList.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final ItemNewsBinding binding;

        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(News news) {
            binding.titleTextNews.setText(news.getTitle());
            binding.summaryTextNews.setText(HtmlCompat.fromHtml(news.getSummary(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            bindDate(news);
            bindCategory(news);
            bindImage(news);
        }

        private void bindDate(News news) {
            LocalDateTime localDateTime = LocalDateTime.parse(news.getDateCreated());
            LocalDate localDate = localDateTime.toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                    .withLocale(Locale.getDefault());

            String formattedDate = localDate.format(formatter);
            binding.dateTextNews.setText(formattedDate);
        }

        private void bindCategory(News news) {
            List<Category> categoryList = news.getCategory();
            if (categoryList != null && !categoryList.isEmpty()) {
                binding.categoriesTextNews.setText(categoryList.get(0).getTitle());
            }
        }

        private void bindImage(News news) {
            List<Image> imageList = news.getImage();
            if (imageList != null && !imageList.isEmpty()) {
                String imageUrl = "https://www.zaragoza.es/cont/paginas/noticias/" + imageList.get(0).getSrc();
                Picasso.get()
                        .load(imageUrl)
                        .resize(1000, 1000)
                        .centerCrop()
                        .placeholder(R.drawable.progress_animation)
                        .into(binding.imageNews);
            }
        }
    }
}