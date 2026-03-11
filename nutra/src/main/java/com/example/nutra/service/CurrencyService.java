package com.example.nutra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String FRANKFURTER_API = "https://api.frankfurter.dev/v1/latest";

    private final RestTemplate restTemplate;

    /**
     * Fetches the latest exchange rates from the Frankfurter API
     * with EUR as the base currency.
     *
     * @return raw API response map containing "amount", "base", "date", and "rates"
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getLatestRates() {
        Map<String, Object> response = restTemplate.getForObject(FRANKFURTER_API, Map.class);
        return response != null ? response : new LinkedHashMap<>();
    }

    /**
     * Converts a value expressed in the base currency (EUR) to the
     * target currency, then returns the converted amount plus
     * useful metadata for the frontend.
     *
     * @param amount         the amount IN BASE CURRENCY (EUR) to convert
     * @param targetCurrency ISO-4217 currency code (e.g. "USD", "INR")
     * @return a map containing: base, targetCurrency, originalAmount,
     *         convertedAmount, exchangeRate, date
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> convert(double amount, String targetCurrency) {
        // Fetch rates from Frankfurter – always EUR-based
        String url = FRANKFURTER_API + "?base=EUR&symbols=" + targetCurrency.toUpperCase();
        Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);

        if (apiResponse == null) {
            throw new RuntimeException("Failed to fetch exchange rates from Frankfurter API.");
        }

        Map<String, Number> rates = (Map<String, Number>) apiResponse.get("rates");
        if (rates == null || !rates.containsKey(targetCurrency.toUpperCase())) {
            throw new IllegalArgumentException("Unsupported currency: " + targetCurrency);
        }

        double exchangeRate = rates.get(targetCurrency.toUpperCase()).doubleValue();
        double convertedAmount = amount * exchangeRate;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("base", apiResponse.get("base"));
        result.put("targetCurrency", targetCurrency.toUpperCase());
        result.put("originalAmount", amount);
        result.put("exchangeRate", exchangeRate);
        result.put("convertedAmount", Math.round(convertedAmount * 100.0) / 100.0);
        result.put("date", apiResponse.get("date"));
        return result;
    }

    /**
     * Returns all supported currencies with their exchange rates
     * relative to EUR so the frontend can populate a currency selector.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllRates() {
        Map<String, Object> apiResponse = restTemplate.getForObject(FRANKFURTER_API, Map.class);
        if (apiResponse == null) {
            throw new RuntimeException("Failed to fetch exchange rates from Frankfurter API.");
        }
        return apiResponse;
    }
}
