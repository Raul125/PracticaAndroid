package com.raulrh.practicaandroid.ui.converter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ConverterFragmentBinding;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ConverterFragment extends Fragment {

    public ConverterFragmentBinding binding;

    private String currency1;
    private String currency2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ConverterFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCurrencySelectionListeners();
        setupConversionButton();
        setupHistoryButtons();
    }

    public void setupCurrencyAdapters(List<String> currencies) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, currencies);
        binding.converterCurrency1.setAdapter(arrayAdapter);
        binding.converterCurrency2.setAdapter(arrayAdapter);
    }

    private void setupCurrencySelectionListeners() {
        binding.converterCurrency1.setOnItemClickListener((parent, view, position, id) -> {
            currency1 = parent.getItemAtPosition(position).toString();
        });

        binding.converterCurrency2.setOnItemClickListener((parent, view, position, id) -> {
            currency2 = parent.getItemAtPosition(position).toString();
        });
    }

    private void setupConversionButton() {
        CurrencyConverter currencyConverter = new CurrencyConverter(this);
        binding.converterButton.setOnClickListener(v -> {
            String text = binding.converterInput.getText().toString();
            if (text.isEmpty())
                return;

            if (currency1 == null || currency2 == null)
                return;

            double amount = Double.parseDouble(text);
            Money moneyToConvert = Money.of(CurrencyUnit.of(currency1), amount);
            Money moneyConverted = currencyConverter.convertCurrency(moneyToConvert, CurrencyUnit.of(currency2));
            binding.converterResult.setText(String.format(moneyConverted.getAmount().toString()));

            String historyEntry = String.format(Locale.getDefault(), "%.2f %s = %.2f %s\n", amount, currency1, moneyConverted.getAmount(), currency2);
            historyEntry += binding.converterHistory.getText();
            binding.converterHistory.setText(historyEntry);
        });
    }

    private void setupHistoryButtons() {
        binding.shareButton.setOnClickListener(v -> {
            String historyText = binding.converterHistory.getText().toString();
            if (historyText.isEmpty()) {
                Toast.makeText(requireContext(), "No hay historial para compartir", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, historyText);
            startActivity(Intent.createChooser(shareIntent, "Compartir historial"));
        });

        binding.cleanButton.setOnClickListener(v -> {
            binding.converterHistory.setText("");
        });
    }
}