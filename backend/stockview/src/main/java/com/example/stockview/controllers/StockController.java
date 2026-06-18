package com.example.stockview.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class StockController {

    @GetMapping("/api/stocks")
    public List<Map<String, Object>> getStocks() {
        return List.of(
            Map.of("ticker", "AAPL", "name", "Apple Inc.", "price", 175.50),
            Map.of("ticker", "TSLA", "name", "Tesla Inc.", "price", 180.20),
            Map.of("ticker", "NVDA", "name", "NVIDIA Corp.", "price", 850.00)
        );
    }
}