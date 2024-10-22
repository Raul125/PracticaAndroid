package com.raulrh.practicaandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.CalculatorFragmentBinding;
import com.raulrh.practicaandroid.databinding.FragmentHomeBinding;
import com.raulrh.practicaandroid.ui.calculator.NumberButtons;
import com.raulrh.practicaandroid.ui.calculator.UtilButtons;

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
    }
}