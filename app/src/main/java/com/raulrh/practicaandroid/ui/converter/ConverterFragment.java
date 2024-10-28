package com.raulrh.practicaandroid.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.CurrencyConverterTask;
import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ConverterFragmentBinding;

import org.javamoney.moneta.convert.ecb.ECBCurrentRateProvider;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

public class ConverterFragment extends Fragment {

    private ConverterFragmentBinding binding;

    String[] currencies = {"Euro", "Dollar", "Libra"};

    private String currency1 = currencies[0];
    private String currency2 = currencies[0];

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ConverterFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, currencies);
        binding.converterCurrency1.setAdapter(arrayAdapter);
        binding.converterCurrency1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currency1 = parent.getItemAtPosition(position).toString();
            }
        });

        binding.converterCurrency2.setAdapter(arrayAdapter);
        binding.converterCurrency2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currency2 = parent.getItemAtPosition(position).toString();
            }
        });

        binding.converterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double number = Double.parseDouble(binding.converterInput.getText().toString());
                new CurrencyConverterTask(binding).execute(number);
            }
        });
    }
}