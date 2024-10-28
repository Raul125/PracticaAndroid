package com.raulrh.practicaandroid.ui.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.databinding.CalculatorFragmentBinding;

import java.text.DecimalFormat;
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
        String text = "";
        if (leftNumber.isEmpty()) {
            text += "0";
        } else {
            text += formatString(leftNumber);
        }

        if (!operator.isEmpty()) {
            text += " " + operator + " ";
        }

        if (!rightNumber.isEmpty()) {
            text += formatString(rightNumber);
        }

        binding.textNumbers.setText(text);
    }

    private String formatString(String number) {
        try {
            double doubleNumber = Double.parseDouble(number);
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
            numberFormat.setMaximumFractionDigits(3);
            return numberFormat.format(doubleNumber);
        } catch (NumberFormatException e) {
            return "0";
        }
    }
}