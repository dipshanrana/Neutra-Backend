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
 * Public endpoints (no auth required):
 * GET /currency/rates – all latest rates (base: EUR)
 * GET /currency/convert – convert a given amount from EUR to a target currency
 */
@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3084" })
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * Returns the full map of exchange rates with EUR as base.
     * Frontend can use this to populate a currency dropdown.
     *
     * Response example:
     * {
     * "amount": 1.0,
     * "base": "EUR",
     * "date": "2026-03-10",
     * "rates": { "USD": 1.1641, "INR": 107.05, ... }
     * }
     */
    @GetMapping("/rates")
    public ResponseEntity<Map<String, Object>> getLatestRates() {
        return ResponseEntity.ok(currencyService.getAllRates());
    }

    /**
     * Converts a given EUR amount into the requested target currency.
     *
     * Query params:
     * amount – the monetary value in EUR (default: 1.0)
     * currency – target ISO-4217 currency code, e.g. "USD", "INR", "GBP"
     *
     * Response example:
     * {
     * "base": "EUR",
     * "targetCurrency": "INR",
     * "originalAmount": 100.0,
     * "exchangeRate": 107.05,
     * "convertedAmount": 10705.0,
     * "date": "2026-03-10"
     * }
     */
    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(
            @RequestParam(defaultValue = "1.0") double amount,
            @RequestParam String currency) {
        Map<String, Object> result = currencyService.convert(amount, currency);
        return ResponseEntity.ok(result);
    }
}
