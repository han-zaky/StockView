package com.example.stockview.dtos;

public record APIStockDto (
    String symbol,
    String name,
    String close,
    String open,
    String high,
    String low,
    String status,
    String message
    ) {

    }
