package com.example.stockview.repositories;

import com.example.stockview.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("""
        SELECT s FROM Stock s WHERE LOWER(s.ticker) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Stock> searchStock(@Param("query") String query);


    Optional<Stock> findByTicker(String ticker);

}