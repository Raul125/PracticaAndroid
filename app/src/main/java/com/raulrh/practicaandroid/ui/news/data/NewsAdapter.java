package com.raulrh.practicaandroid.ui.news.data;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ItemNewsBinding;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

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
            binding.titleTextNews.setText(news.getTitle());
            binding.summaryTextNews.setText(HtmlCompat.fromHtml(news.getSummary(), HtmlCompat.FROM_HTML_MODE_LEGACY));

            LocalDateTime localDateTime = LocalDateTime.parse(news.getDateCreated());
            LocalDate localDate = localDateTime.toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                    .withLocale(Locale.getDefault());

            String formattedDate = localDate.format(formatter);
            binding.dateTextNews.setText(formattedDate);

            List<Category> categoryList = news.getCategories();
            if (categoryList != null && !categoryList.isEmpty()) {
                binding.categoriesTextNews.setText(categoryList.get(0).getTitle());
            }

            List<Image> imageList = news.getImages();
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