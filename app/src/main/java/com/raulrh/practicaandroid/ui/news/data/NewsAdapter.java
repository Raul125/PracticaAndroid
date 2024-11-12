package com.raulrh.practicaandroid.ui.news.data;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.databinding.ItemNewsBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private final List<News> newsList;
    private final NewsClickListener newsClickListener;

    public NewsAdapter(List<News> newsList, NewsClickListener listener) {
        this.newsList = newsList;
        this.newsClickListener = listener;
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

    public interface NewsClickListener {
        void onNewsClick(News news);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final ItemNewsBinding binding;

        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(News news) {
            binding.titleTextNews.setText(news.title);
            binding.summaryTextNews.setText(HtmlCompat.fromHtml(news.summary, HtmlCompat.FROM_HTML_MODE_LEGACY));
            binding.dateTextNews.setText(news.dateCreated);

            if (news.category != null && !news.category.isEmpty()) {
                binding.categoriesTextNews.setText(news.category.get(0).title);
            }

            if (news.image != null && !news.image.isEmpty()) {
                String imageUrl = "https://www.zaragoza.es/cont/paginas/noticias/" + news.image.get(0).src;
                Picasso.get()
                        .load(imageUrl)
                        .resize(1000, 1000)
                        .centerCrop()
                        .into(binding.imageNews);
            }
        }
    }
}