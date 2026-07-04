package com.example.stockview.services;

import com.example.stockview.dtos.StockDto;
import com.example.stockview.repositories.StockRepository;
import com.example.stockview.entities.Stock;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;



@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) { 
        this.stockRepository = stockRepository;
    }

    /* Testing method */
    public List<StockDto> getStocks() {
        return List.of(
            new StockDto("AAPL", "Apple Inc.", 175.50),
            new StockDto("TSLA", "Tesla Inc.", 180.20),
            new StockDto("NVDA", "NVIDIA Corp.", 850.00)
        );
    }

    public List<StockDto> getStockByQuery(String query) {
        return stockRepository.searchStock(query).stream().filter(Objects::nonNull).map(this::stockToDto).toList();
    }

    public Optional<StockDto> getStockByTicker(String ticker) {
        return stockRepository.findByTicker(ticker.toUpperCase()).map(this::stockToDto);
    }

    /* Converts to resposenses */
    private StockDto stockToDto(Stock stock) {
        if (stock == null) {
            return null;
        }
        return new StockDto (stock.getTicker(), stock.getName(), stock.getPrice());
    }

}