package com.example.stockview.services;

import com.example.stockview.dtos.StockDto;
import com.example.stockview.dtos.APIStockDto;
import com.example.stockview.repositories.StockRepository;

import jakarta.transaction.Transactional;

import com.example.stockview.entities.Stock;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



@Service
public class StockService {

    private final StockRepository stockRepository;

    @Value("${twelvedata.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    public StockService(StockRepository stockRepository) { 
        this.stockRepository = stockRepository;
    }

    public List<StockDto> getStockByQuery(String query) {
        return stockRepository.searchStock(query).stream().filter(Objects::nonNull).map(this::stockToDto).toList();
    }

    public Optional<StockDto> getStockByTicker(String ticker) {
        return stockRepository.findByTicker(ticker.toUpperCase()).map(this::stockToDto);
    }

    @Transactional
    public Optional<StockDto> updateStockfromAPI(String ticker) {
        String uppTicker = ticker.toUpperCase();
        try {
            String url = "https://api.twelvedata.com/quote?symbol={ticker}&apikey={apiKey}";
            
            APIStockDto response = restClient.get()
                    .uri(url, uppTicker, apiKey)
                    .retrieve()
                    .body(APIStockDto.class);

            if (response == null || "error".equals(response.status())) {
                System.err.println("Twelve Data API vrátilo chybu pro ticker: " + uppTicker);
                return Optional.empty();
            }

            String name = response.name();
            double price = Double.parseDouble(response.close());
            Optional<Stock> existingStock = stockRepository.findByTicker(uppTicker);
            Stock stockToSave = existingStock.orElseGet(Stock::new); 
        
            stockToSave.setTicker(uppTicker);
            stockToSave.setName(name);
            stockToSave.setPrice(price);
        
            Stock savedStock = stockRepository.save(stockToSave);
            return Optional.of(stockToDto(savedStock));
        } catch (Exception e) {
            System.err.println("Chyba při získávání dat z Twelve Data API pro ticker: " + uppTicker);
            e.printStackTrace();
            return Optional.empty();
        }
    }


        /* Converts to resposenses */
    private StockDto stockToDto(Stock stock) {
        if (stock == null) {
            return null;
        }
        return new StockDto (stock.getTicker(), stock.getName(), stock.getPrice());
    }
}