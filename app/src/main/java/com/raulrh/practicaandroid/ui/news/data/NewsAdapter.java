package com.raulrh.practicaandroid.ui.news.data;

import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.ui.shoppinglist.data.ShoppingItem;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private final List<News> newsList;
    private final NewsClickListener newsClickListener;

    public interface NewsClickListener {
        void onNewsClick(News news);
    }

    public NewsAdapter(List<News> newsList, NewsClickListener listener) {
        this.newsList = newsList;
        this.newsClickListener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news);
        holder.itemView.setOnClickListener(v -> newsClickListener.onNewsClick(news));
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
        private final TextView titleTextView, summaryTextView, dateTextView, categoriesTextView;
        private final ImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            summaryTextView = itemView.findViewById(R.id.summaryTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            categoriesTextView = itemView.findViewById(R.id.categoriesTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(News news) {
            titleTextView.setText(news.title);
            summaryTextView.setText(Html.fromHtml(news.summary));
            dateTextView.setText(news.dateCreated);

            if (news.category != null && !news.category.isEmpty()) {
                categoriesTextView.setText(news.category.get(0).title);
            }

            if (news.image != null && !news.image.isEmpty()) {
                String imageUrl = "https://www.zaragoza.es/cont/paginas/noticias/2024/solidaria2.jpg";

                Glide.with(this)
                        .load(imageUrl)
                        .into(imageView);
            }
        }
    }
}
