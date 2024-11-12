package com.raulrh.practicaandroid.ui.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.databinding.CalculatorFragmentBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class CalculatorFragment extends Fragment {

    private CalculatorFragmentBinding binding;

    private UtilButtons utilButtons;
    private NumberButtons numberButtons;

    public String leftNumber = "";
    public String rightNumber = "";
    public String operator = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CalculatorFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utilButtons = new UtilButtons(this, binding);
        numberButtons = new NumberButtons(this, binding);
    }

    public void update() {
        StringBuilder displayText = new StringBuilder();
        displayText.append(leftNumber.isEmpty() ? "0" : formatNumber(leftNumber));
        if (!operator.isEmpty()) {
            displayText.append(" ").append(operator).append(" ");
        }

        if (!rightNumber.isEmpty()) {
            displayText.append(formatNumber(rightNumber));
        }

        binding.textNumbers.setText(displayText.toString());
    }

    private String formatNumber(String number) {
        try {
            double doubleNumber = Double.parseDouble(number);
            return formatDouble(doubleNumber);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    private String formatDouble(double number) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        numberFormat.setMaximumFractionDigits(3);
        return numberFormat.format(number);
    }
}