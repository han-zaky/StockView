package com.example.stockview.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


import com.example.stockview.services.StockService;
import com.example.stockview.dtos.StockDto;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping 
    public List<StockDto> getStocksByQuery(@RequestParam(value = "search", required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return stockService.getStockByQuery(query);
    }
    
    @GetMapping("/{ticker}")
    public ResponseEntity<StockDto> getStockByTicker(@PathVariable("ticker") String ticker) {
        Optional<StockDto> stock = stockService.getStockByTicker(ticker);
        if (stock.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stock.get());
    }

    
}