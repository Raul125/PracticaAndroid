package com.raulrh.practicaandroid.ui.news;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.raulrh.practicaandroid.databinding.NewsFragmentBinding;
import com.raulrh.practicaandroid.ui.news.data.News;
import com.raulrh.practicaandroid.ui.news.data.NewsAdapter;
import com.raulrh.practicaandroid.ui.news.data.Response;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsFragment extends Fragment {

    private static final String API_URL = "https://www.zaragoza.es/sede/servicio/noticia?rf=html&start=0&rows=5";

    private NewsFragmentBinding binding;
    private NewsAdapter newsAdapter;
    private List<News> newsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = NewsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchNewsData();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(newsList, this);

        binding.recyclerView.setAdapter(newsAdapter);
    }

    private void fetchNewsData() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(responseStream);
                    Response response = new Gson().fromJson(reader, Response.class);
                    mainHandler.post(() -> {
                        newsList = response.getResult();
                        newsAdapter.updateList(newsList);
                        binding.loadingLayout.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    });
                }
            } catch (Exception e) {
                Log.e("NewsFragment", "Error fetching news data", e);
            }
        });
    }
}