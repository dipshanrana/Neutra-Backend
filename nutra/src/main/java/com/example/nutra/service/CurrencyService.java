package com.example.nutra.service;

import com.example.nutra.model.Product;
import com.example.nutra.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String FRANKFURTER_API = "https://api.frankfurter.dev/v1/latest";

    /**
     * Base currency used across the entire store.
     * Admin enters all product prices (SP / MP) in USD.
     */
    private static final String BASE_CURRENCY = "USD";

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;

    // ─────────────────────────────────────────────────────────────────────────
    // GET ALL RATES
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fetches the latest exchange rates from the Frankfurter API
     * with USD as the base currency (matching how admin adds prices).
     *
     * @return raw API response map containing "amount", "base", "date", and "rates"
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllRates() {
        String url = FRANKFURTER_API + "?base=" + BASE_CURRENCY;
        Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);
        if (apiResponse == null) {
            throw new RuntimeException("Failed to fetch exchange rates from Frankfurter API.");
        }
        return apiResponse;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CONVERT SINGLE AMOUNT
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Converts a single USD price to the chosen target currency.
     *
     * @param amount         the price in USD (as stored by admin)
     * @param targetCurrency ISO-4217 target currency code (e.g. "INR", "EUR",
     *                       "GBP")
     * @return a map containing: base, targetCurrency, originalAmount,
     *         convertedAmount, exchangeRate, date
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> convert(double amount, String targetCurrency) {
        String upperTarget = targetCurrency.toUpperCase();

        // If user already selected USD — return as-is, no API call needed
        if (upperTarget.equals(BASE_CURRENCY)) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("base", BASE_CURRENCY);
            result.put("targetCurrency", BASE_CURRENCY);
            result.put("originalAmount", amount);
            result.put("exchangeRate", 1.0);
            result.put("convertedAmount", amount);
            result.put("date", LocalDate.now().toString());
            return result;
        }

        // Fetch USD-based rate for the target currency only
        String url = FRANKFURTER_API + "?base=" + BASE_CURRENCY + "&symbols=" + upperTarget;
        Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);

        if (apiResponse == null) {
            throw new RuntimeException("Failed to fetch exchange rates from Frankfurter API.");
        }

        Map<String, Number> rates = (Map<String, Number>) apiResponse.get("rates");
        if (rates == null || !rates.containsKey(upperTarget)) {
            throw new IllegalArgumentException("Unsupported currency: " + targetCurrency);
        }

        double exchangeRate = rates.get(upperTarget).doubleValue();
        double convertedAmount = amount * exchangeRate;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("base", BASE_CURRENCY);
        result.put("targetCurrency", upperTarget);
        result.put("originalAmount", amount);
        result.put("exchangeRate", exchangeRate);
        result.put("convertedAmount", Math.round(convertedAmount * 100.0) / 100.0);
        result.put("date", apiResponse.get("date"));
        return result;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CONVERT ALL PRODUCT PRICES (SP & MP for all pack sizes)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fetches a product by ID and converts ALL its price fields
     * (singleProductSp, singleProductMp, twoProductSp, twoProductMp,
     * threeProductSp, threeProductMp) from USD to the chosen currency
     * in a single response — saves the frontend from 6 separate calls.
     *
     * @param productId      the product's database ID
     * @param targetCurrency target ISO-4217 currency code
     * @return map with exchangeRate, date, currency, and all converted prices
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertProductPrices(Long productId, String targetCurrency) {
        String upperTarget = targetCurrency.toUpperCase();

        // Load product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        // Determine exchange rate
        double rate;
        String date;

        if (upperTarget.equals(BASE_CURRENCY)) {
            rate = 1.0;
            date = LocalDate.now().toString();
        } else {
            String url = FRANKFURTER_API + "?base=" + BASE_CURRENCY + "&symbols=" + upperTarget;
            Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);
            if (apiResponse == null) {
                throw new RuntimeException("Failed to fetch exchange rates from Frankfurter API.");
            }
            Map<String, Number> rates = (Map<String, Number>) apiResponse.get("rates");
            if (rates == null || !rates.containsKey(upperTarget)) {
                throw new IllegalArgumentException("Unsupported currency: " + targetCurrency);
            }
            rate = rates.get(upperTarget).doubleValue();
            date = (String) apiResponse.get("date");
        }

        // Build response with all converted price fields
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("productId", productId);
        result.put("productName", product.getName());
        result.put("baseCurrency", BASE_CURRENCY);
        result.put("targetCurrency", upperTarget);
        result.put("exchangeRate", rate);
        result.put("date", date);

        // Single pack
        result.put("singleProductMp", convertValue(product.getSingleProductMp(), rate));
        result.put("singleProductSp", convertValue(product.getSingleProductSp(), rate));

        // Two pack
        result.put("twoProductMp", convertValue(product.getTwoProductMp(), rate));
        result.put("twoProductSp", convertValue(product.getTwoProductSp(), rate));

        // Three pack
        result.put("threeProductMp", convertValue(product.getThreeProductMp(), rate));
        result.put("threeProductSp", convertValue(product.getThreeProductSp(), rate));

        return result;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    /** Multiplies a nullable USD value by the exchange rate, rounded to 2 dp. */
    private Double convertValue(Double usdValue, double rate) {
        if (usdValue == null)
            return null;
        return Math.round(usdValue * rate * 100.0) / 100.0;
    }
}
