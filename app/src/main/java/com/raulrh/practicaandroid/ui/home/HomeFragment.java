package com.raulrh.practicaandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        binding.calculator.setOnClickListener(v -> {
            navController.navigate(R.id.nav_calculator);
        });
        binding.converter.setOnClickListener(v -> {
            navController.navigate(R.id.nav_converter);
        });
        binding.shoppingList.setOnClickListener(v -> {
            navController.navigate(R.id.nav_shopping_list);
        });
        binding.news.setOnClickListener(v -> {
            navController.navigate(R.id.nav_news);
        });
        binding.minesweeper.setOnClickListener(v -> {
            navController.navigate(R.id.nav_minesweeper);
        });
        binding.weather.setOnClickListener(v -> {
            navController.navigate(R.id.nav_weather);
        });
    }
}