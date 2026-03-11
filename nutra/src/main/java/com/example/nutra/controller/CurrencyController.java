package com.example.nutra.controller;

import com.example.nutra.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller that exposes currency conversion endpoints
 * powered by the Frankfurter API.
 *
 * All product prices are stored in USD (admin base currency).
 * These endpoints allow the frontend to convert any USD price
 * into the currency chosen by the customer.
 *
 * Public endpoints (no auth required):
 * GET /currency/rates – all latest rates (base: USD)
 * GET /currency/convert – convert a USD amount to a target currency
 */
@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3084" })
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * Returns the full map of exchange rates with USD as base.
     * Use this to populate a currency dropdown on the frontend.
     *
     * Response example:
     * {
     * "amount": 1.0,
     * "base": "USD",
     * "date": "2026-03-10",
     * "rates": { "EUR": 0.85903, "INR": 91.96, "GBP": 0.74345, ... }
     * }
     */
    @GetMapping("/rates")
    public ResponseEntity<Map<String, Object>> getLatestRates() {
        return ResponseEntity.ok(currencyService.getAllRates());
    }

    /**
     * Converts a USD price (as stored by the admin) into the
     * customer's chosen target currency.
     *
     * Query params:
     * amount – the product price in USD (default: 1.0)
     * currency – target ISO-4217 currency code, e.g. "INR", "EUR", "GBP"
     *
     * Response example:
     * {
     * "base": "USD",
     * "targetCurrency": "INR",
     * "originalAmount": 49.99,
     * "exchangeRate": 91.96,
     * "convertedAmount": 4597.52,
     * "date": "2026-03-10"
     * }
     */
    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(
            @RequestParam(defaultValue = "1.0") double amount,
            @RequestParam String currency) {
        return ResponseEntity.ok(currencyService.convert(amount, currency));
    }

    /**
     * Converts ALL price fields of a product (SP & MP for all 3 pack sizes)
     * from USD to the customer's chosen currency — one call, all prices ready.
     *
     * Query params:
     * currency – target ISO-4217 currency code, e.g. "INR", "EUR", "GBP"
     *
     * Response example:
     * {
     * "productId": 1,
     * "productName": "Nutra Omega 3",
     * "baseCurrency": "USD",
     * "targetCurrency": "INR",
     * "exchangeRate": 91.96,
     * "date": "2026-03-11",
     * "singleProductMp": 4597.52,
     * "singleProductSp": 3677.52,
     * "twoProductMp": 8349.52,
     * "twoProductSp": 6897.52,
     * "threeProductMp": 11977.52,
     * "threeProductSp": 9677.52
     * }
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<Map<String, Object>> convertProductPrices(
            @PathVariable Long id,
            @RequestParam String currency) {
        return ResponseEntity.ok(currencyService.convertProductPrices(id, currency));
    }
}
