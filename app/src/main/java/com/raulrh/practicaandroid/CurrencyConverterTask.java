package com.raulrh.practicaandroid;

import android.os.AsyncTask;

import com.raulrh.practicaandroid.databinding.ConverterFragmentBinding;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.convert.ecb.ECBCurrentRateProvider;

public class CurrencyConverterTask extends AsyncTask<Double, Void, MonetaryAmount> {
    private ConverterFragmentBinding binding; // Cambia 'YourActivityBinding' por el nombre real de tu binding

    public CurrencyConverterTask(ConverterFragmentBinding binding) {
        this.binding = binding;
    }

    @Override
    protected MonetaryAmount doInBackground(Double... params) {
        Double number = params[0];
        MonetaryAmount euros = Money.of(number, "EUR");

        try {
            // Crear un proveedor de tasas de cambio
            ExchangeRateProvider imfRateProvider = MonetaryConversions
                    .getExchangeRateProvider("ECB");

            // Obtener la conversión de EUR a USD
            CurrencyConversion conversionUSD = imfRateProvider.getCurrencyConversion("USD");

            // Realizar la conversión
            return euros.with(conversionUSD);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Manejo de errores: retornar null en caso de excepción
        }
    }

    @Override
    protected void onPostExecute(MonetaryAmount pounds) {
        if (pounds != null) {
            // Actualizar la UI con el resultado
            binding.converterResult.setText(pounds.toString());
        } else {
            // Manejo de errores, por ejemplo, mostrar un mensaje de error
            binding.converterResult.setText("Error en la conversión");
        }
    }
}
