package com.raulrh.practicaandroid.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.ConverterFragmentBinding;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class ConverterFragment extends Fragment {

    private ConverterFragmentBinding binding;

    String[] currencies = {"EUR", "USD", "GBP"};

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

        CurrencyConverter currencyConverter = new CurrencyConverter(binding);
        binding.converterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currency2 == null || currency1 == null)
                    return;

                double number = Double.parseDouble(binding.converterInput.getText().toString());

                Money money = Money.of(CurrencyUnit.of(currency1), number);
                Money amountInEur = currencyConverter.convertToEUR(money);
                Money gbp = currencyConverter.convertCurrency(amountInEur, CurrencyUnit.of(currency2));
                binding.converterResult.setText(gbp.getAmount().toString());
            }
        });
    }
}