package com.raulrh.practicaandroid.ui.converter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.raulrh.practicaandroid.R;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CurrencyConverter {

    private static final String ECB_RATES_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private final ConverterFragment converterFragment;
    private final Map<String, Double> exchangeRates;
    private final ExecutorService executorService;
    private final Handler uiHandler;

    public CurrencyConverter(ConverterFragment converterFragment) {
        this.converterFragment = converterFragment;
        this.exchangeRates = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.uiHandler = new Handler(Looper.getMainLooper());
        fetchExchangeRates();
    }

    private void fetchExchangeRates() {
        converterFragment.binding.loadingProgressBar.setVisibility(View.VISIBLE);
        executorService.submit(() -> {
            Map<String, Double> rates = fetchRatesFromApi();
            updateExchangeRates(rates);
        });
    }

    private Map<String, Double> fetchRatesFromApi() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 1.0);
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

    private void updateExchangeRates(Map<String, Double> rates) {
        uiHandler.post(() -> {
            if (!rates.isEmpty()) {
                exchangeRates.putAll(rates);
                converterFragment.setupCurrencyAdapters(exchangeRates.keySet().stream().toList());
                converterFragment.binding.loadingLayout.setVisibility(View.GONE);
                converterFragment.binding.converterLayout.setVisibility(View.VISIBLE);
            } else {
                converterFragment.binding.loadingText.setText(converterFragment.getString(R.string.error_fetching));
            }
        });
    }

    public Money convertCurrency(Money amount, CurrencyUnit toCurrency) {
        String currencyCode = amount.getCurrencyUnit().getCode();
        double rateToEur = exchangeRates.getOrDefault(currencyCode, 0.0);
        Money amountInEur = amount.convertedTo(CurrencyUnit.EUR, BigDecimal.valueOf(1.0 / rateToEur), RoundingMode.HALF_EVEN);

        double rateToTarget = exchangeRates.getOrDefault(toCurrency.getCode(), 0.0);
        return amountInEur.convertedTo(toCurrency, BigDecimal.valueOf(rateToTarget), RoundingMode.HALF_EVEN);
    }
}