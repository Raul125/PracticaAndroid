package com.raulrh.practicaandroid.ui.converter;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.raulrh.practicaandroid.databinding.ConverterFragmentBinding;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CurrencyConverter {

    private static final String ECB_RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private final ConverterFragmentBinding binding;
    private Map<String, Double> exchangeRates;

    public CurrencyConverter(ConverterFragmentBinding binding) {
        exchangeRates = new HashMap<>();
        exchangeRates.put("EUR", 1.0); // El EUR tiene una tasa de 1.0 consigo mismo
        this.binding = binding;
        binding.loadingProgressBar.setVisibility(View.VISIBLE);
        // Ejecutar la tarea para obtener tasas de cambio
        new FetchExchangeRatesTask().execute();
    }

    // Obtener tasas de cambio de la API del BCE
    private class FetchExchangeRatesTask extends AsyncTask<Void, Void, Map<String, Double>> {

        @Override
        protected Map<String, Double> doInBackground(Void... voids) {
            Map<String, Double> rates = new HashMap<>();
            rates.put("EUR", 1.0); // El EUR tiene una tasa de 1.0 consigo mismo

            try {
                URL url = new URL(ECB_RATES_URL);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(url.openStream());
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Cube");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    if (element.hasAttribute("currency") && element.hasAttribute("rate")) {
                        rates.put(element.getAttribute("currency"), Double.parseDouble(element.getAttribute("rate")));
                    }
                }
            } catch (Exception e) {
                Log.e("CurrencyConverter", "Error fetching exchange rates", e);
            }
            return rates;
        }

        @Override
        protected void onPostExecute(Map<String, Double> rates) {
            if (rates != null && !rates.isEmpty()) {
                exchangeRates.putAll(rates); // Actualiza el mapa de tasas con los valores obtenidos
                binding.loadingProgressBar.setVisibility(View.INVISIBLE);
                Log.d("CurrencyConverter", "Tasas obtenidas: " + rates);
            } else {
                Log.e("CurrencyConverter", "No se pudieron obtener las tasas.");
            }
        }
    }

    // Convertir de cualquier moneda a EUR
    public Money convertToEUR(Money amount) {
        double rateToEur = exchangeRates.containsKey(amount.getCurrencyUnit().getCode())
                ? exchangeRates.get(amount.getCurrencyUnit().getCode())
                : 0.0;
        return amount.convertedTo(CurrencyUnit.EUR, BigDecimal.valueOf(1.0 / rateToEur), java.math.RoundingMode.HALF_UP);
    }

    // Convertir de EUR a cualquier otra moneda
    public Money convertFromEUR(Money amount, CurrencyUnit toCurrency) {
        double rateToEur = exchangeRates.containsKey(toCurrency.getCode())
                ? exchangeRates.get(toCurrency.getCode())
                : 0.0;
        return amount.convertedTo(toCurrency, BigDecimal.valueOf(rateToEur), java.math.RoundingMode.HALF_UP);
    }

    // Convertir entre cualquier par de monedas usando EUR como intermediario
    public Money convertCurrency(Money amount, CurrencyUnit toCurrency) {
        Money amountInEur = convertToEUR(amount);
        return convertFromEUR(amountInEur, toCurrency);
    }
}